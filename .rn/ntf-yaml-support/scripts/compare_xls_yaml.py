"""Task #3 サンプリング比較用: XlsDump.java が出力した Excel ダンプ（TSV）と、
nablarch-testing-converter が出力した YAML（1シート=1ファイル）の内容を比較する。

- 対象ブロック: list_maps(LIST_MAP) / setup_tables(SETUP_TABLE) / expected_tables(EXPECTED_TABLE)
  / setup_files(SETUP_VARIABLE・SETUP_FIXED) / expected_files(EXPECTED_VARIABLE・EXPECTED_FIXED)
- Excel ヘッダに同名カラムが複数ある場合は NTF・converter と同じく後方（右側）の列の値を採用し、
  DUP_COLUMN として報告する（前方列の値が後方と異なる行はその値も出力する）。
- 各ブロックの全データ行を列単位で突合し（サンプリングの上位互換）、不一致を報告する。
- 記録用に各 YAML ファイルから 3 行（元データが 3 行未満なら全行）のサンプルを出力する。
- 比較は 2 段階で行う。RAW: Excel セルの生値と YAML 値の完全一致。
  NTF: Excel 値に NTF の Excel 用インタープリタ（NullInterpreter: "null"→null、
  QuotationTrimmer: 外側 1 層の半角/全角ダブルクォート除去）を適用した値と YAML 値の一致。
  converter は Excel 読み込み時に NTF と同じインタープリタを適用して YAML を出力するため、
  RAW 不一致かつ NTF 一致のセルは「表記差（意味は同一）」として RAW_DIFF に分類して報告する。
- YAML は全スカラーを文字列として読む（BaseLoader）。PyYAML 既定の YAML 1.1 解釈では
  `no` 等のキーが真偽値になるが、nablarch-testing-yaml は snakeyaml-engine（YAML 1.2）で文字列として読むため。
- YAML 側に未対応のトップレベルキーがあれば UNCHECKED として報告する。
- マーカーカラム（`[...]` 形式のカラム名）は NTF の HeaderLine が除外しテストデータとして使わないため、
  両側から除いて比較する。ただし全カラムがマーカーの行（converter が行数保持のため
  マーカーカラムだけを出力する行）は、マーカー値を比較し、不一致は MARKER_DIFF として報告する。

使い方: python3 -I compare_xls_yaml.py <dump.tsv> <yaml_dir>
  yaml_dir は converter が出力した <テストクラス名>/ ディレクトリ（中の <シート名>.yaml を比較）。
  dump.tsv は XlsDump.java（.xls / .xlsx 対応）の出力。
終了コード: NTF 解釈後も不一致・未対応ブロック・シート過不足があれば 1、
  RAW_DIFF（表記差）のみなら 2、完全一致なら 0。
"""
import os
import sys

import yaml

HANDLED = {"list_maps", "setup_tables", "expected_tables", "setup_files", "expected_files"}
DUPS = []
# サンプル行の判定表記: 完全一致 / NTF 解釈後一致（外側クォート等の表記差）/ マーカー値の表記差 / 不一致
STATUS = {True: "OK", "RAW": "表記差", "MARKER": "表記差(マーカー)", False: "NG"}


def unescape(v):
    out, i = [], 0
    while i < len(v):
        if v[i] == "\\" and i + 1 < len(v):
            out.append({"n": "\n", "r": "\r", "t": "\t", "\\": "\\"}[v[i + 1]])
            i += 2
        else:
            out.append(v[i])
            i += 1
    return "".join(out)


def load_dump(path):
    sheets = {}
    with open(path, encoding="utf-8") as f:
        for line in f:
            sheet, r, c, v = line.rstrip("\n").split("\t", 3)
            sheets.setdefault(sheet, {})[(int(r), int(c))] = unescape(v)
    return sheets


def ntf_interpret(v):
    """NTF の Excel 用インタープリタ（NullInterpreter → QuotationTrimmer）相当。"""
    if v.lower() == "null":
        return None
    if len(v) >= 2 and ((v[0] == '"' and v[-1] == '"') or (v[0] == "\u201d" and v[-1] == "\u201d")):
        return v[1:-1]
    return v


def is_marker_column(name):
    return name.startswith("[") and name.endswith("]")


def strip_markers(row):
    return {k: v for k, v in row.items() if not is_marker_column(k)}


def find_marker(cells, marker):
    rows = [r for (r, c), v in cells.items() if c == 1 and v == marker]
    if len(rows) != 1:
        raise ValueError(f"marker {marker!r} found {len(rows)} times")
    return rows[0]


def row_cells(cells, r):
    return {c: v for (rr, c), v in cells.items() if rr == r}


def read_table(cells, marker):
    """marker 行の次行をヘッダとし、空行までをデータ行として返す。"""
    r = find_marker(cells, marker)
    header = row_cells(cells, r + 1)
    cols = sorted(header)
    names = [header[c] for c in cols]
    dup_names = sorted({n for n in names if names.count(n) > 1})
    for n in dup_names:
        DUPS.append(f"{marker}: duplicate column {n} at columns {[c for c in cols if header[c] == n]}")
    rows, r = [], r + 2
    while row_cells(cells, r) and not row_cells(cells, r).get(1, "").startswith("//"):
        rc = row_cells(cells, r)
        rows.append({header[c]: rc.get(c, "") for c in cols})  # 同名カラムは後方の列が勝つ
        for n in dup_names:
            vals = [rc.get(c, "") for c in cols if header[c] == n]
            if len(set(vals)) > 1:
                DUPS.append(f"{marker} row{len(rows)} column {n}: values by column order {vals} -> adopted {vals[-1]!r}")
        r += 1
    return names, rows


def read_file_block(cells, marker):
    """SETUP_VARIABLE/SETUP_FIXED: ディレクティブ行 → (レコード種別+フィールド名行, 型行, データ行...) の繰返し。"""
    r = find_marker(cells, marker) + 1
    directives, records = {}, []
    while True:
        rc = row_cells(cells, r)
        if not rc or rc.get(1, "").startswith("//"):
            break
        if 2 in rc and 3 in rc and 1 in rc:  # レコード種別行（フィールド名が2列目以降に並ぶ）
            cols = sorted(c for c in rc if c >= 2)
            rec = {"record_type": rc[1], "fields": [rc[c] for c in cols],
                   "types": [row_cells(cells, r + 1).get(c, "") for c in cols], "rows": []}
            r += 2
            while True:
                dc = row_cells(cells, r)
                if not dc or 1 in dc:
                    break
                rec["rows"].append([dc.get(c, "") for c in cols])
                r += 1
            records.append(rec)
            continue
        directives[rc.get(1)] = rc.get(2, "")
        r += 1
    return directives, records


def table_marker(kind, block, key="table"):
    g = block.get("group_id")
    return f"{kind}[{g}]={block[key]}" if g else f"{kind}={block[key]}"


def compare(dump_path, yaml_dir):
    sheets = load_dump(dump_path)
    errors, raw_diffs, samples, checked, dup_report, marker_diffs = [], [], [], 0, [], []
    yaml_files = sorted(f for f in os.listdir(yaml_dir) if f.endswith(".yaml"))
    yaml_sheets = {f[:-5] for f in yaml_files}
    for s in sorted(set(sheets) - yaml_sheets):
        errors.append(f"sheet {s}: no YAML file")
    for yf in yaml_files:
        sheet = yf[:-5]
        cells = sheets.get(sheet)
        if cells is None:
            errors.append(f"{yf}: no Excel sheet")
            continue
        DUPS.clear()
        data = yaml.load(open(os.path.join(yaml_dir, yf), encoding="utf-8"), Loader=yaml.BaseLoader) or {}
        flat = []  # (block label, key, excel row dict, yaml row dict)
        for k in data:
            if k not in HANDLED:
                errors.append(f"{yf}: UNCHECKED top-level key {k}")
        for b in data.get("list_maps", []):
            _, xrows = read_table(cells, f"LIST_MAP={b['id']}")
            flat.append((f"LIST_MAP={b['id']}", xrows, b["rows"]))
        for kind, key in (("SETUP_TABLE", "setup_tables"), ("EXPECTED_TABLE", "expected_tables")):
            for b in data.get(key, []):
                m = table_marker(kind, b)
                _, xrows = read_table(cells, m)
                flat.append((m, xrows, b["rows"]))
        files = [("SETUP", b) for b in data.get("setup_files", [])] + \
                [("EXPECTED", b) for b in data.get("expected_files", [])]
        for prefix, b in files:
            kind = f"{prefix}_VARIABLE" if b["type"] == "variable" else f"{prefix}_FIXED"
            m = table_marker(kind, b, "path")
            xdir, xrecs = read_file_block(cells, m)
            for dk, dv in xdir.items():
                if b["directives"].get(dk) != dv:
                    errors.append(f"{yf} {m}: directive {dk} excel={dv!r} yaml={b['directives'].get(dk)!r}")
            if len(xrecs) != len(b["records"]):
                errors.append(f"{yf} {m}: record count excel={len(xrecs)} yaml={len(b['records'])}")
            for xr, yr in zip(xrecs, b["records"]):
                if xr["record_type"] != yr["record_type"]:
                    errors.append(f"{yf} {m}: record_type {xr['record_type']!r} != {yr['record_type']!r}")
                yfields = [f["name"] for f in yr["fields"]]
                ytypes = [f["type"] for f in yr["fields"]]
                if xr["fields"] != yfields or xr["types"] != ytypes:
                    errors.append(f"{yf} {m}: fields/types differ")
                flat.append((f"{m} ({xr['record_type']})",
                             [dict(zip(xr["fields"], row)) for row in xr["rows"]],
                             [dict(zip(yfields, row)) for row in yr["rows"]]))
        all_rows = []
        for label, xrows, yrows in flat:
            if len(xrows) != len(yrows):
                errors.append(f"{yf} {label}: row count excel={len(xrows)} yaml={len(yrows)}")
            for i, (xr0, yr0) in enumerate(zip(xrows, yrows), 1):
                checked += 1
                xr, yr = strip_markers(xr0), strip_markers(yr0)
                if not xr and not yr:  # マーカーカラムだけの行
                    xr, yr = xr0, yr0
                    if xr != yr:
                        marker_diffs.append(f"{yf} {label} row{i}: excel={xr} yaml={yr}")
                        all_rows.append((label, i, xr, yr, "MARKER"))
                        continue
                ok = xr == yr
                if not ok:
                    xi = {k: ntf_interpret(v) for k, v in xr.items()}
                    if xi == yr:
                        ok = "RAW"
                        for k in xr:
                            if xr[k] != yr.get(k):
                                raw_diffs.append(f"{yf} {label} row{i} {k}: excel={xr[k]!r} yaml={yr.get(k)!r}")
                    else:
                        errors.append(f"{yf} {label} row{i}: excel={xr} yaml={yr}")
                all_rows.append((label, i, xr, yr, ok))
        dup_report.extend(f"{yf} {d}" for d in DUPS)
        n = len(all_rows)
        picks = all_rows if n <= 3 else [all_rows[0], all_rows[n // 2], all_rows[-1]]
        for label, i, xr, yr, ok in picks:
            key, val = next(iter(xr.items()))
            samples.append((f"{os.path.basename(yaml_dir)}/{yf}", n, f"{label} 行{i} ({len(xr)}列比較)",
                            f"{key}={xr[key]!r}", f"{key}={yr.get(key)!r}", STATUS[ok]))
    return errors, raw_diffs, samples, checked, dup_report, marker_diffs


def main():
    errors, raw_diffs, samples, checked, dup_report, marker_diffs = compare(sys.argv[1], sys.argv[2])
    for d in marker_diffs:
        print("MARKER_DIFF\t" + d)
    for d in dup_report:
        print("DUP_COLUMN\t" + d)
    for s in samples:
        print("SAMPLE\t" + "\t".join(str(x) for x in s))
    for d in raw_diffs:
        print("RAW_DIFF\t" + d)
    for e in errors:
        print("ERROR\t" + e)
    print(f"CHECKED_ROWS\t{checked}\tRAW_DIFF\t{len(raw_diffs)}\tMARKER_DIFF\t{len(marker_diffs)}"
          f"\tERRORS\t{len(errors)}")
    sys.exit(1 if errors else (2 if raw_diffs or marker_diffs else 0))


if __name__ == "__main__":
    main()
