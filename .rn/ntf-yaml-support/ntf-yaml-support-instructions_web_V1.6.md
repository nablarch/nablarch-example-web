# NTF YAML 対応 指示書（汎用テンプレート）

/home/k10949/workspace/配下の **nablarch-example-web** プロジェクトに対して、以下の `#Goal` 以降の内容を実行してください。

※本指示は複数プロジェクトへ同一内容で適用する汎用テンプレートです。対象プロジェクト名・対象ファイル・件数などプロジェクト固有の箇所は、**各セッションで手動書き換えのうえ利用**してください（並列セッションでの同時実行を想定）。

## 運用ルール

- steering.md / checks/task-N.md は、本指示書の **「## MDテンプレート（章立て・構成）」** に記載したテンプレート（章立て・構成）に従って作成すること（隣接プロジェクトを参照しなくても作成できる）。内容は対象プロジェクト向けに調整する。
- ビルド・テストの実行コマンドはプロジェクトにより異なるため、**作業前に対象プロジェクトの README を確認し、正しいビルド・テスト手順を steering.md に記載**したうえで、以降はその手順に従って実行すること（本指示書中の `mvn` コマンドは一例であり、README の記載を優先する）。
- 何かあれば **task毎に各MDファイル（steering.md含む）を更新しながら**進めること。
- user review について: **順調に進んでいる場合は、task 毎の user review を都度実施せず、全ての task 完了後にまとめて実施する**（各 task 完了時は結果を記録しつつ連続して次の task へ進めてよい）。ただし次のいずれかに該当する場合は、その時点で作業を止めてユーザーに確認すること: (1) self-check / 各エキスパートレビューの Verdict が OK でない（NG がある）、(2) テストが FAILURE / BUILD FAILURE となった、(3) その他エラー・想定外の事象が発生した、(4) サンプリング比較で差異が見つかった。これらに該当する場合は修正を試みず（差異時は修正せず）状況を報告して停止すること。
- **本指示書自体も成果物として配置する**: Task #1 で、本指示書ファイルを対象プロジェクトの `.rn/ntf-yaml-support/` 配下へ**同じファイル名でコピー**し（例: `cp <本指示書のパス> .rn/ntf-yaml-support/`）、`cmp` 等で原本と同一内容であることを確認したうえで Task #1 のコミットに含めること。steering.md の Overview には、配置したコピーのパスと原本のパスを記載する。
- チャット（ユーザーへの応答）は日本語で表示すること。
- QA / language / software-engineering の各レビューは、**Agent ツールで `subagent_type: general-purpose`・`model: Opus` を指定してアドホック起動し、該当エキスパートのペルソナを付与**して実施すること（Opus であればバージョンは問わない。Agent ツールの `model` パラメータには `opus` を指定する）。
  - software-engineering レビューでの**重複指摘はMDへの記載不要**（ユーザー指示）。
- check ファイル・steering.md の記載レベルは、本指示書の「## MDテンプレート（章立て・構成）」に従うこと（ユーザー指示）。**全 task の `checks/task-N.md` を同一フォーマット（表形式）で作成する**。具体的には:
  - 見出しは `# task-N Completion Check` とする。
  - **Completion Criteria** は `| Criterion | Self-check | Evidence | QA | QA Evidence |` の5列の表とする。Self-check 列・QA 列には `OK` / `NG` を記載し、QA Evidence には QA エキスパートが独立に確認した事実を記載する。**エビデンス列には日付を記載しない**（事実のみを簡潔に書く）。
  - **QA Expert Review** は `| Aspect | Verdict | Evidence / Improvement |` の表とし、Aspect は `Meaningful tests/verification` / `Edge case coverage` の2行とする。
  - **Expert Reviews (code changes only)** は、コード変更なしのタスクは `N/A — no code changes in this task.` と記載する。コード変更ありは次の小節を設け、それぞれ `| Aspect | Verdict | Evidence / Improvement |` の表とする:
    - **### Language Expert**: `Best practices` / `Codebase style consistency` / `GWT test format`（該当しない観点は Verdict を `N/A`）
    - **### Software-engineering Expert**: `Separation of concerns` / `System integrity` / `Maintainability`
  - **Overall Verdict** は箇条書きで `Self-check` / `QA` / `Language expert` / `Software-engineering expert` / `Ready for user review` を記載する（コード変更なしのタスクは Language / Software-engineering を `N/A`。NG があった場合は `()` で概要を追記）。
  - steering.md の `# Tasks` の章立て・記載レベルは**本指示書の `# Tasks`** に合わせる（Purpose / Prerequisites / Steps / Completion criteria）。Task 毎に内容が異なる点に注意。

## 作成するMDのファイル構成

```
.rn/ntf-yaml-support/<本指示書のファイル名>.md（本指示書のコピー。Task #1 で配置）
.rn/ntf-yaml-support/steering.md
.rn/ntf-yaml-support/checks/task-1.md
.rn/ntf-yaml-support/checks/task-2.md
.rn/ntf-yaml-support/checks/task-3.md
.rn/ntf-yaml-support/checks/task-4.md
.rn/ntf-yaml-support/checks/task-5.md
```

チェック等で Script 作成時（Python 等）は、**何のためのスクリプトなのかを steering.md と task-N.md（N は該当 taskNo）に記載**し、作成した Script は `.rn/ntf-yaml-support/scripts/` 配下に格納すること。

## MDテンプレート（章立て・構成）

隣接プロジェクトを参照しなくても、以下の章立て・構成をテンプレートとして steering.md / checks/task-N.md を作成できる。`<...>` は対象プロジェクト向けに埋める。

### steering.md のテンプレート

```markdown
# <プロジェクト名> NTF YAML 対応 steering

## Overview
- 目的・背景（Goal / Acceptance criteria の要約）
- 対象プロジェクト名
- 指示書: `.rn/ntf-yaml-support/<本指示書のファイル名>.md`（原本: `<原本のパス>`）

## Build / Test
- README で確認したビルド・テストの実行手順（プロファイル・generate-resources の要否・テストコマンド）を明記する

## Tasks
（本指示書の `# Tasks` と同じ章立て・記載レベル。Task 毎に内容を調整する）

### #N: <タスク名>
**Purpose**: <目的>
**Prerequisites**: <前提タスク or none>
**Steps**:
- [ ] <手順>（task 進捗に応じて [x] へ更新する）
**Completion criteria**:
- <完了条件>
```

### checks/task-N.md のテンプレート

```markdown
# task-N Completion Check

## Completion Criteria

| Criterion | Self-check | Evidence | QA | QA Evidence |
|---|---|---|---|---|
| <完了条件> | OK / NG | <事実のみを簡潔に。日付は記載しない> | OK / NG | <QA が独立に確認した事実> |

## QA Expert Review

| Aspect | Verdict | Evidence / Improvement |
|---|---|---|
| Meaningful tests/verification | OK / NG | <所見・改善点> |
| Edge case coverage | OK / NG | <所見・改善点> |

## Expert Reviews (code changes only)

（コード変更なしのタスクは `N/A — no code changes in this task.` と記載し、以下の小節は省略する）

### Language Expert

| Aspect | Verdict | Evidence / Improvement |
|---|---|---|
| Best practices | OK / NG / N/A | <所見・改善点> |
| Codebase style consistency | OK / NG / N/A | <所見・改善点> |
| GWT test format | OK / NG / N/A | <所見・改善点> |

### Software-engineering Expert

| Aspect | Verdict | Evidence / Improvement |
|---|---|---|
| Separation of concerns | OK / NG | <所見・改善点> |
| System integrity | OK / NG | <所見・改善点> |
| Maintainability | OK / NG | <所見・改善点> |

（software-engineering レビューでの重複指摘は記載不要）

## Overall Verdict

- Self-check: OK / NG
- QA: OK / NG
- Language expert: OK / NG / N/A
- Software-engineering expert: OK / NG / N/A
- Ready for user review: Yes / No
（NG があった場合は各行に `()` で概要を追記）
```

#### task固有セクション（該当タスクのみ追記）

Task #3 の `checks/task-3.md` には、上記テンプレートに加えて次のセクションを追記する（サンプリング比較・併存チェックの詳細を記録するため）。

```markdown
## Sampling Comparison（task-3 のみ）
| YAML ファイル | 比較件数 | レコードキー/行 | Excel 値 | YAML 値 | 一致 |
| --- | --- | --- | --- | --- | --- |
| <path> | 3（元データが3件未満なら全件） | <key/行> | <value> | <value> | OK / NG |
（差異があれば原因を調査し、修正せずユーザーへ報告して停止する）

## .xls/.xlsx 併存チェック（該当時のみ）
- 併存の有無: <あり / なし>
- 差分確認結果の保存先: `.rn/ntf-yaml-support/...`（POMなし一時ディレクトリで個別変換した比較結果）
- 未使用側（通常 `.xlsx`）の削除: <変換前に削除済み / 該当なし> → ユーザーへ報告・判断待ち
```

---

# Goal

対象プロジェクト：nablarch-example-web

NTF（Nablarch Testing Framework）のAI対応として、AIが読み書きできないバイナリ形式のExcelテストデータをYAML形式に移行する。

背景資料: https://github.com/Fintan-contents/nablarch-system-development-guide/pull/211

# Acceptance criteria

- **本作業は、ブランチに過去の NTF YAML 移行コミット（完了済みの成果物）が既に積まれている場合でも、必ず再実施する。完了済みを検知しても「検証のみ」で終わらせてはならない（スキップ禁止）。** 既存のコミット履歴は書き換え・rebase・amend・hard reset をせず維持したまま、新しいコミット（ベースラインコミット → task-1〜5）を**既存履歴の上に積み上げる**ことで再実施する
- 既存の `ntf-yaml-support` ブランチ上で（コミット履歴を維持したまま）全作業が行われている（ブランチの新規作成は行わない）
- 作業開始時に、ブランチの**現在の先端（過去の移行コミットを含む）の上に**、ワーキングツリー全体を `develop` と同一内容へ揃えるコミットを1つ**新規に積んでいる**（ベースライン化。`develop` に存在しないファイルは削除、差分ファイルは `develop` の内容へ置換し、ツリーが `develop` と完全一致した状態にする。※ブランチを `develop` へ巻き戻すのではなく、`develop` 一致ツリーを持つ新コミットを既存履歴の上に積む）
- 作業前に、`develop` と同一状態（ベースライン）で既存の全テストがパスすることを確認済みである
- `pom.xml` に `nablarch-testing-yaml:1.0.0-SNAPSHOT` が test スコープの依存として追加されている
- `pom.xml` に `nablarch-testing-converter:1.0.0-SNAPSHOT` が `<build><plugins>` のビルドプラグイン（plugin直下 `<configuration>` に変換パラメータを設定し、CLIで `convert` ゴールを実行）として追加されている（test依存ではない。converter は Maven プラグインであり、テスト実行時には不要で XLS→YAML 変換時のみ使用するため。web/batch テンプレートに準拠）
- テスト実行中に `testDataParser` が参照される全フェーズで YAML が有効になるよう、`YamlTestDataParser` コンポーネント（`testDataParser`）が適切な設定ファイルに追加されている（通常は `unit-test.xml`。同期送信系など実行中にモックがテストデータを読むテストでは、アクション起動 diConfig（`*-boot.xml`）側にも＝可能なら集約して配置。Rules 参照）。`interpreters` プロパティは公式ドキュメント nablarch-document の `development_tools/testing_framework/setup/testdata.html`「テストデータの形式をYAMLに変更する」の設定例に合わせ、コンポーネント内に `dateTimeInterpreter` → `compositeInterpreter` の順で2つのみを直接記載する
- 変換対象の全てのテスト用Excelファイルがそれぞれ対応するYAMLファイルに変換されている（サンプリングでExcelとYAMLの内容が一致することを確認済み）
- 変換後、変換対象だったExcelファイルが全て削除されている
- YAMLテストデータを使った状態で全テストがパスしている

# Assumptions

- `nablarch-testing-yaml:1.0.0-SNAPSHOT` と `nablarch-testing-converter:1.0.0-SNAPSHOT` はローカルMavenリポジトリ（`~/.m2`）に存在する（確認済み）
- テスト実行にはH2インメモリDBを使用し、DBセットアップ済みである
- JDK / Maven / H2 などビルド・テスト実行環境は整備済みである
- `develop` ブランチは存在する前提とする
- `ntf-yaml-support` ブランチは既存で、過去作業のコミット履歴を持つ前提とする（本作業ではこの履歴を維持し、ブランチの再作成は行わない）
- YAMLファイルの出力先は `src/test/java` 配下（Excelと同じ場所）とし、`nablarch.test.resource-root` の設定変更は不要
- 変換ツール（`nablarch-testing-converter`）の実行手順は `/home/k10949/workspace/nablarch-testing-converter/README.md` に従う
- 対象プロジェクトに変換対象のExcelが存在する前提とする（実施時に所在を確認する）

# Rules

- commit every change; one completion marker per task
- push は行わない（コミットのみ実施し、リモートへの push は最後にユーザーが行う）
- テストクラス（`.java`）は変更しない
- **完了済みでも必ず再実施する（スキップ禁止）**: ブランチに過去の NTF YAML 移行コミットが既に積まれ、受け入れ基準を満たしている状態であっても、本指示書の task-1〜5 を最初から再実施すること。「既に完了済み」を理由に検証のみで終えてはならない。
- 既存の `ntf-yaml-support` ブランチ上で作業する（ブランチの新規作成はしない。コミット履歴を維持する）
- **過去のコミット履歴は維持する（書き換え禁止）**: `git rebase` / `git commit --amend` / `git reset --hard` / force push などで既存コミットを改変・削除してはならない。再実施分は常に**新しいコミットとして既存履歴の上に積み上げる**。
- 作業開始時に、ブランチの**現在の先端（過去の移行コミットを含む）の上に**、ワーキングツリー全体を `develop` と同一内容へ揃えるコミットを1つ**新規に**積む（ベースライン化）。`develop` に存在しないファイルは削除、差分ファイルは `develop` の内容へ置換し、ツリーが `develop` と完全一致した状態にする（例: `git switch ntf-yaml-support`（巻き戻しはしない）→ `git read-tree -u --reset develop` → `git commit`。または `git rm -r --cached .` → `git checkout develop -- .` → `git commit`）。※ `git reset --hard develop` のようにブランチ先端を `develop` へ移動する操作は履歴を壊すため**禁止**。あくまで `develop` 一致ツリーを持つ新コミットを既存履歴の上に積む。以降の YAML 化作業はこのベースラインコミットの上に積む
- steering.md は各タスクで更新し、当該タスクのコミットに含める
- ビルド・テストの実行コマンドは**対象プロジェクトの README を優先**する（`mvn -P gsp clean generate-resources && mvn test` の順序は一例。プロジェクトによりプロファイルや手順が異なる）
- テストはExcelファイルが存在しない状態で最終パスを確認する
- YAML変換後はサンプリングでExcelとYAMLの内容を比較してから削除する
- サンプリング比較で差異が見つかった場合は修正せずユーザーに報告して停止する
- `YamlTestDataParser` の `interpreters` は、公式ドキュメント nablarch-document の `development_tools/testing_framework/setup/testdata.html`「テストデータの形式をYAMLに変更する」の設定例に合わせて記載する。
  - `interpreters` プロパティの中に `<list>` を直接記載する（名前付きの `<list name="yamlInterpreters">` を別に定義して参照する書き方はしない）。
  - `interpreters` には `dateTimeInterpreter` と `compositeInterpreter` の2つのみを指定する（他の interpreter は追加しない）。
  - `dbInfo` プロパティに `dbInfo` を指定する。`testDataReader` は指定しない（`YamlTestDataParser` は YAML ファイルを直接読み込むため使用しない）。
  - **定義順は `dateTimeInterpreter` → `compositeInterpreter` とする**（上記設定例、および NTF 標準 `nablarch/test/test-data-interpreter.xml` と同じ前後関係）。`compositeInterpreter` は値に `${...}` を含むとそこで解釈を終え後続へ委譲しないため、先に置くと `${systemTime}` などのシステム時刻記法が `dateTimeInterpreter` に渡らず、未解釈の文字列のまま期待値比較されテストが失敗する。
  - 例（testdata.html の設定例と同じ）:
    ```xml
    <!-- YamlTestDataParser でtestDataParserを上書き -->
    <component name="testDataParser" class="nablarch.test.core.reader.YamlTestDataParser">
      <property name="dbInfo" ref="dbInfo"/>
      <property name="interpreters">
        <list>
          <component-ref name="dateTimeInterpreter"/>
          <component-ref name="compositeInterpreter"/>
        </list>
      </property>
    </component>
    ```
- `YamlTestDataParser` の設定は「どのファイルに書くか」を固定せず、**テスト実行中に `testDataParser` が参照される全フェーズで YAML リーダーが有効になる**ように配置する。
  - セットアップ/検証フェーズ … テスト設定リポジトリ（通常 `unit-test.xml`）
  - アクション実行フェーズ … テスト対象アクションを起動する diConfig（例: `*-boot.xml`）。リクエスト単体テストはアクションを diConfig で起動する際に `SystemRepository` をその diConfig で再初期化する（`MainForRequestTesting`）。この間 `unit-test.xml` は無効。
  - **判定**: 実行中にモック等が `testDataParser` 経由でテストデータを読むか。具体的には「応答電文（`response_*` / sendSync の受信電文）を持つ同期送信系テスト」は該当。（送信のみの非同期・応答不要テストや、通常の web/batch は非該当で `unit-test.xml` だけで足りる）
  - **配置方針**: 該当する場合は diConfig（`*-boot.xml`）側に `YamlTestDataParser` を定義する。`unit-test.xml` が当該 diConfig を import している場合は **diConfig 側に集約（単一ソース化）**すればセットアップ/実行/検証の全フェーズに効く（`unit-test.xml` 側の重複定義は置かない）。import していない構成なら両方に定義する。
  - 本番用の boot（`src/main/resources` 配下）には**入れない**（テスト専用設定は `src/test/resources` 側のみ）。

# Tasks

### #1: ベースライン化（developと同一状態）し、全PASSを確認する

**Purpose**: 既存の `ntf-yaml-support` ブランチ（コミット履歴を維持）上で、**過去の移行コミットの有無にかかわらず**、ブランチ先端の上にワーキングツリー全体を `develop` と同一内容へ揃えるコミットを1つ新規に積み、その `develop` 同等の状態で全テストがパスすることを確認する。※ブランチに過去の NTF YAML 移行コミットが既に積まれ受け入れ基準を満たしていても、本タスク以降を**必ず再実施**すること（完了済みを理由にスキップ・検証のみで終えない）。

**Prerequisites**: none

**Steps**:

- [ ] 既存の `ntf-yaml-support` ブランチへ切り替える（新規作成はしない。コミット履歴を維持する）
- [ ] ブランチに過去の移行コミットが積まれていても**巻き戻さず**、現在の先端の上に処理を積む（`git rebase` / `git reset --hard` / `--amend` は使わない）
- [ ] `develop` ブランチの存在を確認し、ブランチ先端の上にワーキングツリー全体を `develop` と同一内容へ揃える新規コミットを1つ積む（`develop` に無いファイルは削除、差分ファイルは `develop` の内容へ置換し、ツリーが `develop` と完全一致した状態にする。例: `git read-tree -u --reset develop` → `git commit`、または `git rm -r --cached .` → `git checkout develop -- .` → `git commit`。※ `git reset --hard develop` は履歴を壊すため禁止）
- [ ] 対象プロジェクトの README を確認し、ビルド・テストの実行手順（例: `generate-resources` の要否やプロファイル指定）を把握する
- [ ] 隣接プロジェクトの `.rn/` 配下MDを参照し、steering.md と scripts の初版を作成する（steering.md には README で確認したビルド・テスト手順を明記する）
- [ ] 本指示書を `.rn/ntf-yaml-support/` 配下へ同じファイル名でコピーし、原本と同一内容であることを確認する（steering.md の Overview にコピーと原本のパスを記載する）
- [ ] README で確認した手順に従いエンティティクラスを生成する（例: `mvn -P gsp clean generate-resources`）
- [ ] README で確認した手順でテストを実行し、全テストがパスすることを確認する（例: `mvn test`。失敗があれば報告して止まる）
- [ ] 結果を `checks/task-1.md` に記録する（self-check列のみ）
- [ ] 変更ファイル（check ファイル、更新した steering.md、配置した指示書のコピー）をコミットする
- [ ] self-check (OK/NG per completion criterion, record in checks/task-1.md)
- [ ] QA expert review (Agent: general-purpose / Opus)
- [ ] user review

**Completion criteria**:

- 既存の `ntf-yaml-support` ブランチ上で作業しており、コミット履歴が維持されている（ブランチの再作成・rebase・amend・hard reset を行っていない。過去の移行コミットが残っている）
- ブランチの**既存先端の上に**、ワーキングツリー全体が `develop` と同一内容になる新規コミットが1つ積まれている
- `mvn test` が全テストパスで終了している（BUILD SUCCESS）
- 本指示書のコピーが `.rn/ntf-yaml-support/` 配下に原本と同一内容で配置され、コミットされている

### #2: 設定変更 — pom.xml と unit-test.xml にYAML対応を追加

**Purpose**: `pom.xml` に `nablarch-testing-yaml`（test 依存）を追加し、`nablarch-testing-converter` を `<build><plugins>` のビルドプラグイン（plugin直下 `<configuration>` に変換パラメータを設定し、CLIで `convert` ゴールを実行）として追加し、`unit-test.xml` に `YamlTestDataParser` コンポーネントの設定を追加する。`YamlTestDataParser` の設定（`interpreters` を含む）は公式ドキュメント nablarch-document の `development_tools/testing_framework/setup/testdata.html`「テストデータの形式をYAMLに変更する」の設定例に合わせる（pom.xml は隣接プロジェクト（nablarch-example-web / nablarch-example-batch）のYAML対応設定を参考にする）。converter はテスト実行時には不要で XLS→YAML 変換時のみ使う Maven プラグインのため、test 依存ではなくビルドプラグインとして宣言する。設定の配置先は Rules の「`testDataParser` が参照される全フェーズで有効化」方針に従う。

**Prerequisites**: #1

**Steps**:

- [ ] `pom.xml` に `nablarch-testing-yaml:1.0.0-SNAPSHOT`（test scope）を追加する
- [ ] `pom.xml` の `<build><plugins>` に `nablarch-testing-converter:1.0.0-SNAPSHOT` をビルドプラグインとして追加する（`<configuration>` に from=xls / to=yaml / input / output / overwrite を設定。web/batch 準拠）
- [ ] 本テストが「アクションを diConfig で起動し、実行中にモック等が `testDataParser` 経由でテストデータ（応答電文など）を読む」構成かを判定する（応答電文 `response_*` / sendSync の有無で判断）
- [ ] 判定結果に応じて `YamlTestDataParser`（`testDataParser` 上書き）を配置する（非該当: `unit-test.xml`。該当: アクション起動 diConfig（`*-boot.xml`）側に＝可能なら集約。Rules 参照）。`interpreters` は testdata.html の設定例に合わせ、`interpreters` プロパティ内の `<list>` に `dateTimeInterpreter` → `compositeInterpreter` の順で2つのみを直接記載する（順番の理由と記載例は Rules 参照）。※ 定義順は `test-data.xml` の import より後・`dbInfo` 定義より後に置き参照解決を担保する
- [ ] `mvn validate` / `mvn dependency:resolve` などで pom とyaml依存が解決できること、`mvn help:describe -Dplugin=com.nablarch.framework:nablarch-testing-converter:1.0.0-SNAPSHOT` で converter プラグインの `convert` ゴールが解決できることを確認する
- [ ] 変更したファイルをコミットする
- [ ] self-check (OK/NG per completion criterion, record in checks/task-2.md)
- [ ] QA expert review (Agent: general-purpose / Opus)
- [ ] language expert review (Agent: general-purpose / Opus)
- [ ] software-engineering expert review (Agent: general-purpose / Opus)
- [ ] user review

**Completion criteria**:

- `pom.xml` に `nablarch-testing-yaml:1.0.0-SNAPSHOT` が test スコープで追加されている
- `pom.xml` の `<build><plugins>` に `nablarch-testing-converter:1.0.0-SNAPSHOT` がビルドプラグイン（plugin直下 `<configuration>` に変換パラメータを設定し、CLIで `convert` ゴールを実行）として追加されている
- `testDataParser` コンポーネント（`YamlTestDataParser`）が、`testDataParser` が参照される全フェーズで有効になる設定ファイルに追加されている（配置先は Rules の方針に従う）。`interpreters` は testdata.html の設定例どおり、`interpreters` プロパティ内の `<list>` に `dateTimeInterpreter` → `compositeInterpreter` の順で2つのみが記載されている
- `mvn dependency:resolve` が成功し（yaml依存の解決エラーなし）、converter プラグインの `convert` ゴールが `mvn help:describe` で解決できる

### #3: ExcelテストデータをYAMLに変換

**Purpose**: `nablarch-testing-converter` を使って ExcelのテストデータをYAML形式に変換し、各テストのソースツリー内に配置して、変換内容をサンプリングで確認する。

**Prerequisites**: #2

**Steps**:

- [ ] 対象 Excel ファイルの所在を確認する（存在しない場合は変換対象なしとしてユーザーに報告し停止する）
- [ ] **同名の `.xls` と `.xlsx` が併存していないか確認する**。併存する場合、converter は "same-name Excel books coexist" エラーで変換を拒否する（1テストクラス＝1 Excel ブック）。NTF の `PoiXlsReader` は `.xls` を優先解決するため `.xlsx` はシャドウされた未使用ファイル。内容が異なる可能性があるため、両者をPOMなし一時ディレクトリで個別にYAML変換して差分を確認し（比較結果を `.rn/.../` 配下に保存）、未使用側（通常 `.xlsx`）を**変換前に削除**してからユーザーに報告・判断を仰ぐこと。確認・削除の結果は `checks/task-3.md` の **「## .xls/.xlsx 併存チェック」** セクションに記録する。
- [ ] `/home/k10949/workspace/nablarch-testing-converter/README.md` の手順に従い、全ての対象ExcelファイルをYAML変換する
- [ ] 変換後、各YAMLファイルについてExcelの内容とサンプリングで比較し、一致を確認する（各ファイル最低3件。元データが3件未満の場合は元データ件数分）
- [ ] サンプリング比較結果を `checks/task-3.md` の **「## Sampling Comparison」** セクション（テンプレートの task固有セクション参照）に記録する（差異があれば原因を調査しユーザーに報告し、修正せずに止める）
- [ ] 変換したYAMLファイルをコミットする（Excelはまだ削除しない）
- [ ] self-check (OK/NG per completion criterion, record in checks/task-3.md)
- [ ] QA expert review (Agent: general-purpose / Opus)
- [ ] user review

**Completion criteria**:

- 変換対象の全てのExcelファイルに対応するYAMLファイルが生成されている
- サンプリング比較（各ファイル最低3件、元データが3件未満の場合は元データ件数分）でExcelとYAMLの内容が一致している（差異があれば原因を調査しユーザーに報告し、修正せずに止める）
- 変換後のYAMLファイルがコミットされている

### #4: Excelを削除する

**Purpose**: 変換対象だった Excel を削除してコミットする。

**Prerequisites**: #3

**Steps**:

- [ ] 変換対象となった Excel ファイルを削除する
- [ ] 削除結果（残存Excelが0件であること）を `checks/task-4.md` に記録する（Excelなしでの `mvn clean test` 実行とその結果の確認は Task #5 で行う。Task #4 では記載しない）
- [ ] Excelファイル削除をコミットする
- [ ] self-check (OK/NG per completion criterion, record in checks/task-4.md)
- [ ] QA expert review (Agent: general-purpose / Opus)
- [ ] user review

**Completion criteria**:

- 変換対象だった全 Excel ファイルが削除されている
- 削除がコミットされている

### #5: YAMLテストデータで全テストをパスさせる

**Purpose**: Excelなしで全テストがパスすることを確認する。

**Prerequisites**: #4

**Steps**:

- [ ] `mvn test` が BUILD SUCCESS になることを確認する
- [ ] テストログで YamlTestDataParser が実際に使われていることを確認する
- [ ] self-check (OK/NG per completion criterion, record in checks/task-5.md)
- [ ] QA expert review (Agent: general-purpose / Opus)
- [ ] language expert review (Agent: general-purpose / Opus)
- [ ] software-engineering expert review (Agent: general-purpose / Opus)
- [ ] user review

**Completion criteria**:

- `mvn test` が BUILD SUCCESS で終了する（全テストパス、Excel ファイルなし）
- テストログで YamlTestDataParser が実際に使われていることが確認できる
