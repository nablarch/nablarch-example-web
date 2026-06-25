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

- [ ] 各 xlsx について、代表的なシート・行をいくつかピックアップしてYAMLと突き合わせ、値・型・行数が一致することを確認する（全件でなくサンプリングで可）
- [ ] 不一致があれば報告して止まる
- [ ] 全 `.xlsx` ファイルを削除する
- [ ] 削除をコミット・プッシュする
- [ ] self-check (OK/NG per completion criterion, record in checks/task-4.md)
- [ ] QA expert review (subagent)
- [ ] user review

**Completion criteria**:

- サンプリングした全行でExcelとYAMLの値・行数が一致している
- 全 `.xlsx` ファイルが削除されている
- 削除がコミット・プッシュされている

### #5: unit-test.xml に YamlTestDataParser を設定してYAMLテストデータで全テストをパスさせる

**Purpose**: `unit-test.xml` に `YamlTestDataParser` を設定し、xlsx なしで全テストがパスすることを確認する。

**Prerequisites**: #4

**Steps**:

- [ ] `src/test/resources/unit-test.xml` に `YamlTestDataParser` の設定を追加する（PR #211 の `climan-project/unit-test.xml` 変更を参照）
- [ ] `mvn test` が BUILD SUCCESS になることを確認する
- [ ] テストログで YamlTestDataParser が実際に使われていることを確認する
- [ ] self-check (OK/NG per completion criterion, record in checks/task-5.md)
- [ ] QA expert review (subagent)
- [ ] language expert review (subagent)
- [ ] software-engineering expert review (subagent)
- [ ] user review

**Completion criteria**:

- `src/test/resources/unit-test.xml` に `testDataParser` として `YamlTestDataParser` が定義されている
- `mvn test` が BUILD SUCCESS で終了する（全テストパス、xlsx なし）
- テストログで YamlTestDataParser が実際に使われていることが確認できる

# Decisions

# State

- **Status**: not suspended
- **Date**: 2026-06-25
- **Last completed**: #3 ExcelテストデータをYAMLに変換（50ファイル、全スキーマ検証済み）
- **Next**: #4 サンプリング確認 → xlsx 削除
- **Notes**: user review 待ち。
