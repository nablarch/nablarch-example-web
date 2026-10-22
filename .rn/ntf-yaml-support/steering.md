# nablarch-example-web NTF YAML 対応 steering

## Overview
- 目的・背景: NTF（Nablarch Testing Framework）のAI対応として、AIが読み書きできないバイナリ形式のExcelテストデータをYAML形式に移行する（背景資料: https://github.com/Fintan-contents/nablarch-system-development-guide/pull/211）。
  - Acceptance criteria 要約: 既存 `ntf-yaml-support` ブランチの履歴を維持したまま、先端の上に develop 一致ツリーのベースラインコミットを積み、ベースラインで全テストPASSを確認 → `pom.xml` に `nablarch-testing-yaml`（test依存）と `nablarch-testing-converter`（ビルドプラグイン）を追加 → `YamlTestDataParser`（`testDataParser`）を全フェーズで有効になる設定ファイルへ追加 → 全テスト用ExcelをYAMLへ変換（サンプリング一致確認）→ Excel削除 → YAMLのみで全テストPASS。
  - 本ブランチには過去の NTF YAML 移行コミット（〜`f96c145`）が既に存在するが、指示書に従い task-1〜5 を最初から再実施する（スキップしない）。
- 対象プロジェクト名: nablarch-example-web
- 指示書: `.rn/ntf-yaml-support/ntf-yaml-support-instructions_web_V1.6.md`（原本: `/home/k10949/instructions/ntf-yaml-support-instructions_web_V1.6.md`）
- develop ブランチ: ローカル `develop`（`791ef9e`、`origin/develop` と同一）を使用。

## Build / Test
- README（「3. アプリケーションのビルドと起動」「コマンドの補足」）より: Java 17 / Maven 3.9.9以降。H2 組み込み DB を使用。
- DB セットアップおよびエンティティクラス生成（gsp-dba-maven-plugin）は `generate-resources` フェーズにバインドされており、個別実行は `mvn generate-resources`。`compile` / `test` にも含まれる。gsp プロファイル指定は不要（pom.xml に gsp プロファイルは存在しない）。
- gsp プラグインを Java 17 で動かすための JVM オプションは `.mvn/jvm.config` で指定済み。
- テスト実行コマンド: `mvn -B clean test`（clean → generate-resources（gsp-dba）→ compile → test を一括実行）
- ベースラインのテスト件数: **Tests run: 151**（Task #1）。Task #5 ではこの件数と比較し、YAML 移行でテストが脱落していないことを確認する。
- converter 実行コマンド（Task #3）: `mvn com.nablarch.framework:nablarch-testing-converter:1.0.0-SNAPSHOT:convert`（パラメータは pom.xml の plugin `<configuration>` に設定。手順は `/home/k10949/workspace/nablarch-testing-converter/README.md`）

## Scripts
- `.rn/ntf-yaml-support/scripts/` 配下に格納する。作成したスクリプトは用途をここと該当 `checks/task-N.md` に記載する。
- 実行ログ等の証跡は `.rn/ntf-yaml-support/sampling/` 配下に保存する。
  - `sampling/task1-test-log.txt`（Task #1）: ベースラインでの `mvn -B clean test` ログ（先頭行は実行時の HEAD）
  - `sampling/task2-resolve-log.txt`（Task #2）: `dependency:resolve` / `dependency:list` / `help:describe` の結果
  - `sampling/convert-log.txt`（Task #3）: converter 実行ログ
  - `sampling/task5-test-log.txt`（Task #5）: Excel 削除後の `mvn -B clean test` ログ（1行目は実行時の HEAD と Excel 件数）
  - `sampling/dump-<テストクラス>.tsv` / `sampling/compare-<テストクラス>.txt`（Task #3）: Excel ダンプと比較結果
- `scripts/XlsDump.java`（Task #3）: サンプリング比較用。`.xls` / `.xlsx` の全シートの非空セルを TSV（シート/行/列/値）でダンプする（POI 3.8 の `WorkbookFactory` と `DataFormatter` による表示値）。`java -cp poi-3.8.jar:poi-ooxml-3.8.jar:poi-ooxml-schemas-3.8.jar:xmlbeans-2.3.0.jar:dom4j-1.6.1.jar XlsDump.java <file>`
- `scripts/compare_xls_yaml.py`（Task #3）: サンプリング比較用。XlsDump の TSV と converter 出力 YAML を全データ行・全列で突合し、各 YAML から3行（3件未満は全件）をサンプル出力する。生値比較と NTF 解釈（NullInterpreter / QuotationTrimmer）後の比較、マーカーカラム除外、同名カラムの後勝ち検出を行う。`python3 -I compare_xls_yaml.py <dump.tsv> <yaml_dir>`（nablarch-example-batch の同名スクリプトを拡張）

## Status
- Task #1 完了（self-check / QA OK）
- Task #2 完了（self-check / QA / language / software-engineering OK、コミット `89fac0e`）
- Task #3 完了: サンプリング比較で表記差3種類を検出 → ユーザー判断で一致として扱う（コミット `c63b9fb`、QA OK）
- Task #4 完了（コミット `161fce6`、QA OK）
- Task #5: Excel なしで全151件パス。レビュー実施中
- 今後の検討事項（レビューでの任意改善提案）: `nablarch-testing-yaml` / `nablarch-testing-converter` は `1.0.0-SNAPSHOT` を直書きしており pom にリポジトリ定義がないため、正式版公開時に BOM 管理または property 化してバージョンを差し替える。converter plugin への用途コメント追加

## Tasks

### #1: ベースライン化（developと同一状態）し、全PASSを確認する
**Purpose**: 既存の `ntf-yaml-support` ブランチ（コミット履歴を維持）上で、過去の移行コミットの有無にかかわらず、ブランチ先端の上にワーキングツリー全体を `develop` と同一内容へ揃えるコミットを1つ新規に積み、その `develop` 同等の状態で全テストがパスすることを確認する。
**Prerequisites**: none
**Steps**:
- [x] 既存の `ntf-yaml-support` ブランチへ切り替える（新規作成なし。先端 `f96c145` = `origin/ntf-yaml-support`）
- [x] 過去の移行コミットを巻き戻さず、現在の先端の上に処理を積む（rebase / reset --hard / amend 不使用）
- [x] `develop` の存在を確認し、`git read-tree -u --reset develop` → `git commit` でベースラインコミット `c3ae393` を積む（`git diff --quiet develop HEAD` で一致を確認）
- [x] README を確認し、ビルド・テスト手順を把握する（上記 Build / Test）
- [x] steering.md と scripts ディレクトリの初版を作成する
- [x] 本指示書を `.rn/ntf-yaml-support/` へ同名コピーし、`cmp` で原本と同一を確認する
- [x] エンティティクラスを生成する（`mvn -B generate-resources`、および `mvn -B clean test` 内の generate-resources）
- [x] テストを実行し、全テストがパスすることを確認する（Tests run: 151, Failures: 0, Errors: 0 / BUILD SUCCESS）
- [x] 結果を `checks/task-1.md` に記録する
- [x] 変更ファイルをコミットする（`ce397e1`）
- [x] self-check
- [x] QA expert review (Agent: general-purpose / Opus) → OK
- [ ] user review（全 task 完了後にまとめて実施）
**Completion criteria**:
- 既存の `ntf-yaml-support` ブランチ上で作業しており、コミット履歴が維持されている（過去の移行コミットが残っている）
- ブランチの既存先端の上に、ワーキングツリー全体が `develop` と同一内容になる新規コミットが1つ積まれている
- `mvn test` が全テストパスで終了している（BUILD SUCCESS）
- 本指示書のコピーが `.rn/ntf-yaml-support/` 配下に原本と同一内容で配置され、コミットされている

### #2: 設定変更 — pom.xml と unit-test.xml にYAML対応を追加
**Purpose**: `pom.xml` に `nablarch-testing-yaml`（test 依存）と `nablarch-testing-converter`（`<build><plugins>` のビルドプラグイン）を追加し、`YamlTestDataParser`（`testDataParser`）を testdata.html の設定例どおりに、`testDataParser` が参照される全フェーズで有効になる設定ファイルへ追加する。
**Prerequisites**: #1
**Steps**:
- [x] `pom.xml` に `nablarch-testing-yaml:1.0.0-SNAPSHOT`（test scope）を追加する（`nablarch-testing-jetty12` の直後）
- [x] `pom.xml` の `<build><plugins>` に `nablarch-testing-converter:1.0.0-SNAPSHOT` を追加する（plugin 直下 `<configuration>` に from=xls / to=yaml / input=output=`${project.basedir}/src/test/java` / overwrite=true。`<executions>` なし）
- [x] 実行中にモック等が `testDataParser` 経由でテストデータを読む構成か判定する → 非該当（`response_*`・sendSync・メッセージングモックなし。HTTP リクエスト単体テスト / Entity テストのみ）
- [x] 判定結果に応じて `YamlTestDataParser` を配置する → `src/test/resources/unit-test.xml` の `dbInfo` 定義直後に追加（`interpreters` は `dateTimeInterpreter` → `compositeInterpreter` の2つのみ。本番用 `src/main/resources` は変更なし）
- [x] `mvn dependency:resolve` と `mvn help:describe` で yaml 依存・converter `convert` ゴールの解決を確認する（ログ: `sampling/task2-resolve-log.txt`）
- [x] 変更したファイルをコミットする
- [x] self-check
- [x] QA expert review (Agent: general-purpose / Opus) → OK
- [x] language expert review (Agent: general-purpose / Opus) → OK
- [x] software-engineering expert review (Agent: general-purpose / Opus) → OK
- [ ] user review（全 task 完了後にまとめて実施）
**Completion criteria**:
- `pom.xml` に `nablarch-testing-yaml:1.0.0-SNAPSHOT` が test スコープで追加されている
- `pom.xml` の `<build><plugins>` に `nablarch-testing-converter:1.0.0-SNAPSHOT` がビルドプラグイン（plugin直下 `<configuration>` に変換パラメータを設定し、CLIで `convert` ゴールを実行）として追加されている
- `testDataParser` コンポーネント（`YamlTestDataParser`）が、`testDataParser` が参照される全フェーズで有効になる設定ファイルに追加されている。`interpreters` は `<list>` に `dateTimeInterpreter` → `compositeInterpreter` の順で2つのみ
- `mvn dependency:resolve` が成功し、converter プラグインの `convert` ゴールが `mvn help:describe` で解決できる

### #3: ExcelテストデータをYAMLに変換
**Purpose**: `nablarch-testing-converter` を使って ExcelのテストデータをYAML形式に変換し、各テストのソースツリー内に配置して、変換内容をサンプリングで確認する。
**Prerequisites**: #2
**Steps**:
- [x] 対象 Excel ファイルの所在を確認する（`src/test/java/com/nablarch/example/app/web/` の action / dto / form に `.xlsx` 6件・`.xls` 9件）
- [x] 同名の `.xls` と `.xlsx` が併存していないか確認する → 併存なし
- [x] converter README の手順に従い、全ての対象ExcelファイルをYAML変換する（15件 → YAML 70件。ログ: `sampling/convert-log.txt`）
- [x] 各YAMLファイルについてExcelの内容とサンプリングで比較する（全 YAML の全データ行 1,736行を突合し、各 YAML から3行をサンプル記録）
- [x] サンプリング比較結果を `checks/task-3.md` に記録する → 表記差3種類（外側クォート除去 / マーカー値 `1`→`1.0` / 数値セル `2000`→`2000.0`）を検出。NTF 解釈後の不一致は0件。修正せずユーザーへ報告し、**ユーザー判断により一致として扱う**
- [x] 変換したYAMLファイルをコミットする（Excelはまだ削除しない）
- [x] self-check
- [x] QA expert review (Agent: general-purpose / Opus) → OK
- [ ] user review（全 task 完了後にまとめて実施）
**Completion criteria**:
- 変換対象の全てのExcelファイルに対応するYAMLファイルが生成されている
- サンプリング比較（各ファイル最低3件、元データが3件未満の場合は元データ件数分）でExcelとYAMLの内容が一致している
- 変換後のYAMLファイルがコミットされている

### #4: Excelを削除する
**Purpose**: 変換対象だった Excel を削除してコミットする。
**Prerequisites**: #3
**Steps**:
- [x] 変換対象となった Excel ファイルを削除する（15件を `git rm`）
- [x] 削除結果（残存Excelが0件であること）を `checks/task-4.md` に記録する
- [x] Excelファイル削除をコミットする（`161fce6`）
- [x] self-check
- [x] QA expert review (Agent: general-purpose / Opus) → OK
- [ ] user review（全 task 完了後にまとめて実施）
**Completion criteria**:
- 変換対象だった全 Excel ファイルが削除されている
- 削除がコミットされている

### #5: YAMLテストデータで全テストをパスさせる
**Purpose**: Excelなしで全テストがパスすることを確認する。
**Prerequisites**: #4
**Steps**:
- [x] `mvn test` が BUILD SUCCESS になることを確認する（`mvn -B clean test`: Tests run: 151, Failures: 0, Errors: 0 / BUILD SUCCESS。ベースラインと同数）
- [x] テストログで YamlTestDataParser が実際に使われていることを確認する（testDataParser の BasicTestDataParser → YamlTestDataParser 上書きログ、YamlTestDataParser のロガー出力）
- [x] self-check
- [ ] QA expert review (Agent: general-purpose / Opus)
- [ ] language expert review (Agent: general-purpose / Opus)
- [ ] software-engineering expert review (Agent: general-purpose / Opus)
- [ ] user review（全 task 完了後にまとめて実施）
**Completion criteria**:
- `mvn test` が BUILD SUCCESS で終了する（全テストパス、Excel ファイルなし）
- テストログで YamlTestDataParser が実際に使われていることが確認できる
