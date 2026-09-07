# Goal

NTF（Nablarch Testing Framework）のAI対応として、`nablarch-example-web` の既存のExcelテストデータをYAML形式に移行する。
具体的には、`nablarch-testing-converter` を使って 6 つのExcelテストデータ（`.xlsx`）をYAMLに変換し、`nablarch-testing-yaml` を用いた設定変更のみで全テストがパスすることを確認する。
参照PR: https://github.com/Fintan-contents/nablarch-system-development-guide/pull/211

# Acceptance criteria

- 全6つの `.xlsx` テストデータが変換後のYAMLテストデータに置き換えられ、テストクラスと同じディレクトリに配置されている
- `unit-test.xml` に `YamlTestDataParser` の設定が追加されている
- テストクラス本体（`.java` ファイル）の変更はなく、設定ファイルと依存関係の変更のみで移行が完結している
- `mvn test` が BUILD SUCCESS で終了する（リグレッションなし）
- 変換済みのYAMLファイルがリポジトリにコミットされている
- `pom.xml` に `nablarch-testing-yaml` と `nablarch-testing-converter` のtest依存が追加されている

# Assumptions

- `nablarch-testing-yaml:1.0.0-SNAPSHOT` および `nablarch-testing-converter:1.0.0-SNAPSHOT` はローカルの Maven リポジトリに既にインストール済み（`mvn install` 済み）
- `nablarch-example-web` は H2 データベースを使用しており、`mvn test` 前に gsp プロファイルでのエンティティ生成が必要な場合がある
- YAMLファイルの出力先は `src/test/java` 配下（Excelと同じ場所）とし、`nablarch.test.resource-root` の設定変更は不要
- 変換ツール（`nablarch-testing-converter`）は Maven exec plugin またはプログラム的呼び出しで使用する
- 変更対象はこのリポジトリ（`nablarch-example-web`）のみ

# Rules

- commit and push every change; one completion marker per task
- テストクラス（`.java`）は変更しない
- ブランチ `ntf-yaml-support` で作業し、全変更をPRに含める
- 作業ディレクトリ: `/home/tie303177/work/nablarch/nablarch-example-web`
- Java: OpenJDK 17（プロジェクトのJavaバージョン）
- ビルドは `mvn -P gsp clean generate-resources && mvn test` の順序が必要な場合あり（README確認済み）
- 推測で作業しない。READMEやPR #211の内容を参照してから進める

# Tasks

### #1: 現状のビルド・テストで全PASSを確認する

**Purpose**: 移行前のベースラインとして、`nablarch-example-web` のビルドとテストが全てパスすることを確認する。

**Prerequisites**: none

**Steps**:

- [x] `mvn -P gsp clean generate-resources` を実行してエンティティクラスを生成する
- [x] `mvn test` を実行する
- [x] 全テストがパスすることを確認する（失敗があれば報告して止まる）
- [x] self-check (OK/NG per completion criterion, record in checks/task-1.md)
- [x] QA expert review (subagent)
- [ ] user review

**Completion criteria**:

- `mvn test` が BUILD SUCCESS で終了する

### #2: pom.xml に nablarch-testing-yaml / nablarch-testing-converter の依存を追加する

**Purpose**: `pom.xml` に `nablarch-testing-yaml` と `nablarch-testing-converter` をtest依存として追加する。

**Prerequisites**: #1

**Steps**:

- [x] `pom.xml` に `nablarch-testing-yaml:1.0.0-SNAPSHOT` をtest scopeで追加する
- [x] `pom.xml` に `nablarch-testing-converter:1.0.0-SNAPSHOT` をtest scopeで追加する
- [x] `mvn dependency:resolve` で依存が解決できることを確認する
- [x] self-check (OK/NG per completion criterion, record in checks/task-2.md)
- [x] QA expert review (subagent)
- [x] software-engineering expert review (subagent)
- [ ] user review

**Completion criteria**:

- `pom.xml` に `nablarch-testing-yaml` と `nablarch-testing-converter` のtest依存が追加されている
- `mvn dependency:resolve` が成功する

### #3: ExcelテストデータをYAMLに変換してリポジトリに配置する

**Purpose**: `nablarch-testing-converter` を使って6つのExcelテストデータをYAMLに変換し、各テストのソースツリー内に配置する。

**Prerequisites**: #2

**Steps**:

- [x] `AuthenticationActionRequestTest.xlsx` をYAMLに変換する
- [x] `ClientActionTest.xlsx` をYAMLに変換する
- [x] `IndustryActionTest.xlsx` をYAMLに変換する
- [x] `ProjectActionRequestTest.xlsx` をYAMLに変換する
- [x] `ProjectBulkActionRequestTest.xlsx` をYAMLに変換する
- [x] `ProjectUploadActionRequestTest.xlsx` をYAMLに変換する
- [x] 変換済みのYAMLファイルを各テストクラスと同じディレクトリ（`src/test/java/.../`）に配置する
- [x] 変換済みYAMLファイルをgitに追加してコミットする
- [x] self-check (OK/NG per completion criterion, record in checks/task-3.md)
- [x] QA expert review (subagent)
- [ ] user review

**Completion criteria**:

- 6つの変換済みYAMLが `src/test/java/com/nablarch/example/app/web/action/` 配下の各テストクラス名サブディレクトリに存在する
- 変換されたYAMLがスキーマに対して有効である（変換ツールが検証済み）
- 変換済みYAMLがgitでtracked filesとして存在する

### #4: ExcelとYAMLの内容一致をサンプリング確認して xlsx を削除する

**Purpose**: 変換済みYAMLがExcelの内容を正しく再現していることをサンプリングで確認し、xlsx を削除してコミット・プッシュする。

**Prerequisites**: #3

**Steps**:

- [x] 各 xlsx について、代表的なシート・行をいくつかピックアップしてYAMLと突き合わせ、値・型・行数が一致することを確認する（全件でなくサンプリングで可）
- [x] 不一致があれば報告して止まる
- [x] 全 `.xlsx` ファイルを削除する
- [x] 削除をコミット・プッシュする
- [x] self-check (OK/NG per completion criterion, record in checks/task-4.md)
- [x] QA expert review (subagent)
- [x] user review

**Completion criteria**:

- サンプリングした全行でExcelとYAMLの値・行数が一致している
- 全 `.xlsx` ファイルが削除されている
- 削除がコミット・プッシュされている

### #5: unit-test.xml に YamlTestDataParser を設定してYAMLテストデータで全テストをパスさせる

**Purpose**: `unit-test.xml` に `YamlTestDataParser` を設定し、xlsx なしで全テストがパスすることを確認する。

**Prerequisites**: #4

**Steps**:

- [x] `src/test/resources/unit-test.xml` に `YamlTestDataParser` の設定を追加する（PR #211 の `climan-project/unit-test.xml` 変更を参照）
- [x] `mvn test` が BUILD SUCCESS になることを確認する
- [x] テストログで YamlTestDataParser が実際に使われていることを確認する
- [x] self-check (OK/NG per completion criterion, record in checks/task-5.md)
- [x] QA expert review (subagent)
- [x] language expert review (subagent)
- [x] software-engineering expert review (subagent)
- [ ] user review

**Completion criteria**:

- `src/test/resources/unit-test.xml` に `testDataParser` として `YamlTestDataParser` が定義されている
- `mvn test` が BUILD SUCCESS で終了する（全テストパス、xlsx なし）
- テストログで YamlTestDataParser が実際に使われていることが確認できる

### #6: 空の requestParams をマーカーカラムだけの行に直す

**Purpose**: リクエストパラメータが無いテストショットの `requestParams` が `- {}`（空マッピング）で書かれており、`nablarch-testing-yaml` の空行スキップ以後は 0 行扱いとなってテストが失敗する。解説書が定める書き方（マーカーカラムだけの行）に直す。

**Prerequisites**: #5

**由来**: `YamlTableDataBuilder` は「値を 1 つも持たない行（空マッピング `{}`）」を列名解決より前に `dropBlankRows` で取り除く（`nablarch-testing-yaml` `feature/ntf-yaml` `YamlTableDataBuilder.java:37-40`@c8180f2 の Javadoc）。このため `requestParams` の `- {}` 行は 0 行になり、`TestCaseInfo.getRequestParameters`（`nablarch-testing` `TestCaseInfo.java:344-351`）の `request.size() < caseNo` に該当して `IllegalArgumentException: Request parameter is not defined or request parameter list size is invalid.` を送出する。正しい書き方は解説書 `nablarch-document` `ntf-yaml-support` の `ja/development_tools/testing_framework/implementation/testdata_examples.rst:757-768`（マーカーカラムのキーは YAML の配列構文との衝突を避けるためダブルクォートで囲む。例 `- "[no]": "1"`）。

**変更**: `src/test/java` 配下の YAML 33 ファイル・34 行。`list_maps` の `id: "requestParams"` エントリ内の `- {}` を `- "[no]": "<テストショット番号>"` に置換した。`ProjectActionRequestTest/updateAbNormal.yaml` のみ 2 行あり、テストショット no.1／no.2 に対応させて `"1"`・`"2"` を採番した（`getRequestParameters` はケース番号を添字として `request.get(caseNo - 1)` で引くため、行数がテストショット数に足りないと同じ例外になる）。`requestParams` 以外の `- {}` は 0 件であり、置換対象外の書き換えはない。

**検証**:

- [x] `~/work/nablarch/nablarch-testing-yaml`（`feature/ntf-yaml` `c8180f2`、`e984103` 以降）を `mvn -DskipTests install` して `~/.m2` を更新
- [x] 修正前 `mvn clean test`: `Tests run: 151, Failures: 0, Errors: 33` (BUILD FAILURE)。surefire レポートで全 33 件が `TestCaseInfo.getRequestParameters(TestCaseInfo.java:346)` 由来の `IllegalArgumentException` であることを確認（`AuthenticationActionRequestTest` 3 件／`ProjectActionRequestTest` 21 件／`ProjectBulkActionRequestTest` 7 件／`ProjectUploadActionRequestTest` 2 件）
- [x] 置換後 `grep -rn -- '- {}' src/test/java` が 0 件
- [x] 修正後 `mvn clean test`: `Tests run: 151, Failures: 0, Errors: 0, Skipped: 0` (BUILD SUCCESS)
- [x] `git status --short` 空・push 済み

**Completion criteria**:

- `requestParams` の空マッピング行が解説書どおりのマーカーカラム行に置き換わっている
- `mvn clean test` が 151 件全件パスで BUILD SUCCESS

**承認**: 2026-09-07、ディレクター（`ntf-doc-renewal-b5`）が承認。scratchpad に本リポジトリを clone して独立に検証済み — `b5ed1fe` で `mvn clean test` が 151 件中 `Errors: 33`（4 クラスの surefire レポートに `Request parameter is not defined` の例外）、`8a5fe0e` で 151 件 `Errors: 0`。差分は YAML 33 ファイル 34 行と `steering.md` のみで `- {}` の残存 0 件、`updateAbNormal.yaml` の `"1"`／`"2"` 採番もテストショット 2 件に対して妥当と判定。指示書 `ntf-step4-15-example-web-request-params.md` の定めによりサブエージェントレビューは実施していない（機械的置換のため）。

# Decisions

## `downloadNormal.yaml` の `COST_OF_GOODS_SOLD: "2000.0"` について

NTF 仕様（`ntf-testdata-doc.md` 8.1節）では「Excel セルは必ず文字列書式」が要件。元の xlsx のセルが数値書式だったため、コンバーターが `cell.toString()` で `"2000.0"` を出力した。修正するなら xlsx 側（セルを文字列書式に直してから再変換）だが、xlsx はすでに削除済みのため修正対象がない。`"2000.0"` のまま運用する（H2 が INTEGER カラムへ暗黙変換するためテストに影響なし）。**この判断は確定。再度議題にしない。**

# State

- **Status**: paused
- **Date**: 2026-09-07
- **Last completed**: task #6（空の `requestParams` をマーカーカラム行に是正）完了。ディレクター独立検証のうえ承認済み（`8a5fe0e`）
- **Next**: #5 のユーザーレビュー → 承認 → Acceptance criteria 実行
- **Notes**: task #6 は承認済みで残作業なし。task #5 はレビュー完了・ユーザー承認待ちのまま（task #6 の承認とは別件）。
