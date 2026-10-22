# task-1 Completion Check

## Completion Criteria

| Criterion | Self-check | Evidence | QA | QA Evidence |
|---|---|---|---|---|
| 既存の `ntf-yaml-support` ブランチ上で作業し、コミット履歴が維持されている | OK | 作業ブランチは `ntf-yaml-support`（新規作成なし）。旧先端 `f96c145`（= `origin/ntf-yaml-support`）がベースラインコミット `c3ae393` の親であり、過去の移行コミットが `git log` に残存 | | |
| 既存先端の上に、ワーキングツリー全体が `develop` と同一内容になる新規コミットが1つ積まれている | OK | `git read-tree -u --reset develop` → `git commit` で `c3ae393` を作成。`git diff --quiet develop HEAD` が 0、`git merge-base --is-ancestor f96c145 HEAD` が 0 | | |
| `mvn test` が全テストパスで終了している（BUILD SUCCESS） | OK | `mvn -B clean test`（gsp-dba の generate-resources を含む）で Tests run: 151, Failures: 0, Errors: 0, Skipped: 0 / BUILD SUCCESS。ログ: `sampling/task1-test-log.txt`（先頭行に実行時 HEAD `c3ae393`） | | |
| 本指示書のコピーが `.rn/ntf-yaml-support/` 配下に原本と同一内容で配置され、コミットされている | OK | `.rn/ntf-yaml-support/ntf-yaml-support-instructions_web_V1.6.md` を配置し、`cmp` で原本と差分なし。task-1 コミットに含める | | |

## QA Expert Review

| Aspect | Verdict | Evidence / Improvement |
|---|---|---|
| Meaningful tests/verification | | |
| Edge case coverage | | |

## Expert Reviews (code changes only)

N/A — no code changes in this task.

## Overall Verdict

- Self-check: OK
- QA:
- Language expert: N/A
- Software-engineering expert: N/A
- Ready for user review:
