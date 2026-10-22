# task-3 Completion Check

## Completion Criteria

| Criterion | Self-check | Evidence | QA | QA Evidence |
|---|---|---|---|---|
| 変換対象の全てのExcelファイルに対応するYAMLファイルが生成されている | OK | `src/test/java` 配下の Excel 15件（`.xlsx` 6件 / `.xls` 9件）を `mvn com.nablarch.framework:nablarch-testing-converter:1.0.0-SNAPSHOT:convert` で変換し `Converted 15 file(s).` / BUILD SUCCESS。YAML 70件を生成（1シート=1ファイル）。比較スクリプトで Excel 全シートに YAML が対応し、過不足なし。ブロック数も Excel マーカーと一致（LIST_MAP 189 / SETUP_TABLE 50 / EXPECTED_TABLE 12 / EXPECTED_VARIABLE 1） | | |
| サンプリング比較でExcelとYAMLの内容が一致している | OK | 全 YAML 70件の全データ行（1,736行）を突合し、各 YAML から3行（3件未満は全件）をサンプル記録。NTF 解釈後の不一致は0件。生値の表記差3種類（下記「検出した差異」）を修正せずユーザーへ報告し、ユーザー判断により一致とみなす | | |
| 変換後のYAMLファイルがコミットされている | OK | YAML 70件・scripts 2本・sampling（ダンプ・比較結果・変換ログ）・checks/task-3.md・steering.md を task-3 コミットに含める | | |

## QA Expert Review

| Aspect | Verdict | Evidence / Improvement |
|---|---|---|
| Meaningful tests/verification | | |
| Edge case coverage | | |

## Expert Reviews (code changes only)

N/A — no code changes in this task.

## Sampling Comparison（task-3 のみ）

比較スクリプト: `.rn/ntf-yaml-support/scripts/XlsDump.java`（Excel（.xls / .xlsx）全セルを TSV ダンプ）＋ `.rn/ntf-yaml-support/scripts/compare_xls_yaml.py`（ダンプと YAML を全データ行・全列で突合し、各 YAML から3行をサンプルとして出力）。結果: `.rn/ntf-yaml-support/sampling/compare-*.txt`、ダンプ: `sampling/dump-*.tsv`、変換ログ: `sampling/convert-log.txt`。

一致欄: `OK` = 生値で完全一致、`表記差` = NTF 解釈（外側ダブルクォート除去）後に一致、`表記差(マーカー)` = マーカーカラムだけの行でマーカー値の表記が異なる（NTF はマーカーカラムを使用しない）。

| YAML ファイル | 比較件数 | レコードキー/行 | Excel 値 | YAML 値 | 一致 |
| --- | --- | --- | --- | --- | --- |
| action/AuthenticationActionRequestTest/indexNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='login' | REQUEST_ID='login' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/AuthenticationActionRequestTest/loginAbNormal.yaml | 3（全11行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行2 (2列比較) | loginId='10000001' | loginId='10000001' | OK |
| 〃 | 〃 | SETUP_TABLE[3]=USERS 行1 (3列比較) | USER_ID='105' | USER_ID='105' | OK |
| action/AuthenticationActionRequestTest/loginGetNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='login' | REQUEST_ID='login' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/AuthenticationActionRequestTest/loginPostNormal.yaml | 3（全7行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=userContext1 行1 (2列比較) | name='userId' | name='userId' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=USERS 行1 (3列比較) | USER_ID='105' | USER_ID='105' | OK |
| action/AuthenticationActionRequestTest/logoutNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='logout' | REQUEST_ID='logout' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ClientActionTest/setUpDb.yaml | 3（全213行中） | SETUP_TABLE=INDUSTRY 行1 (2列比較) | INDUSTRY_CODE='01' | INDUSTRY_CODE='01' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行14 (3列比較) | CLIENT_ID='14' | CLIENT_ID='14' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行120 (3列比較) | CLIENT_ID='120' | CLIENT_ID='120' | OK |
| form/ClientSearchFormTest/testCharsetAndLength.yaml | 2（元データ2件のため全件） | LIST_MAP=charsetAndLength 行1 (20列比較) | propertyName='clientName' | propertyName='clientName' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行2 (20列比較) | propertyName='industryCode' | propertyName='industryCode' | OK |
| form/ClientSearchFormTest/testSingleValidation.yaml | 3（全36行中） | LIST_MAP=singleValidation 行1 (3列比較) | propertyName='sortKey' | propertyName='sortKey' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行19 (3列比較) | propertyName='sortDir' | propertyName='sortDir' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行36 (3列比較) | propertyName='sortDir' | propertyName='sortDir' | OK |
| action/IndustryActionTest/setUpDb.yaml | 3（全213行中） | SETUP_TABLE=INDUSTRY 行1 (2列比較) | INDUSTRY_CODE='01' | INDUSTRY_CODE='01' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行14 (3列比較) | CLIENT_ID='14' | CLIENT_ID='14' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行120 (3列比較) | CLIENT_ID='120' | CLIENT_ID='120' | OK |
| form/InnerProjectFormTest/testBeanValidation.yaml | 3（全10行中） | LIST_MAP=testShots 行1 (4列比較) | title='正常系-1' | title='正常系-1' | OK |
| 〃 | 〃 | LIST_MAP=params 行1 (5列比較) | projectId='${半角数字, 9}' | projectId='${半角数字, 9}' | OK |
| 〃 | 〃 | LIST_MAP=params 行5 (5列比較) | projectId='${半角数字, 9}' | projectId='${半角数字, 9}' | OK |
| form/InnerProjectFormTest/testCharsetAndLength.yaml | 1（元データ1件のため全件） | LIST_MAP=charsetAndLength 行1 (20列比較) | propertyName='projectName' | propertyName='projectName' | OK |
| form/InnerProjectFormTest/testSingleValidation.yaml | 3（全68行中） | LIST_MAP=singleValidation 行1 (3列比較) | propertyName='projectId' | propertyName='projectId' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行35 (3列比較) | propertyName='projectStartDate' | propertyName='projectStartDate' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行68 (3列比較) | propertyName='projectEndDate' | propertyName='projectEndDate' | OK |
| form/LoginFormTest/testCharsetAndLength.yaml | 2（元データ2件のため全件） | LIST_MAP=charsetAndLength 行1 (21列比較) | propertyName='loginId' | propertyName='loginId' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行2 (21列比較) | propertyName='userPassword' | propertyName='userPassword' | OK |
| action/ProjectActionRequestTest/backToEditNormal.yaml | 3（全17行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=project1 行6 (2列比較) | name='projectEndDate' | name='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=project1 行14 (2列比較) | name='version' | name='version' | OK |
| action/ProjectActionRequestTest/backToNewNormal.yaml | 3（全17行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=project1 行6 (2列比較) | name='projectEndDate' | name='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=project1 行14 (2列比較) | name='version' | name='version' | OK |
| action/ProjectActionRequestTest/completeOfCreateNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='completeOfCreate' | REQUEST_ID='completeOfCreate' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectActionRequestTest/completeOfDeleteNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='completeOfDelete' | REQUEST_ID='completeOfDelete' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectActionRequestTest/completeOfUpdateNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='completeOfUpdate' | REQUEST_ID='completeOfUpdate' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectActionRequestTest/confirmOfCreateAbNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='confirmOfCreate' | REQUEST_ID='confirmOfCreate' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (4列比較) | form.projectName='' | form.projectName='' | OK |
| action/ProjectActionRequestTest/confirmOfCreateNormal.yaml | 3（全17行中） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=project1 行6 (2列比較) | name='projectEndDate' | name='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=project1 行14 (2列比較) | name='version' | name='version' | OK |
| action/ProjectActionRequestTest/confirmOfUpdateAbNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='confirmOfUpdate' | REQUEST_ID='confirmOfUpdate' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (4列比較) | form.projectName='プロジェクト００１' | form.projectName='プロジェクト００１' | OK |
| action/ProjectActionRequestTest/confirmOfUpdateNormal.yaml | 3（全17行中） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=project1 行6 (2列比較) | name='projectEndDate' | name='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=project1 行14 (2列比較) | name='version' | name='version' | OK |
| action/ProjectActionRequestTest/createAbNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='create' | REQUEST_ID='create' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectActionRequestTest/createNormal.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | EXPECTED_TABLE[1]=PROJECT 行1 (15列比較) | PROJECT_NAME='プロジェクト００１' | PROJECT_NAME='プロジェクト００１' | OK |
| action/ProjectActionRequestTest/deleteAbNormal.yaml | 3（全5行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | EXPECTED_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectActionRequestTest/deleteNormal.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectActionRequestTest/downloadAbNormal.yaml | 3（全6行中） | LIST_MAP=testShots 行1 (11列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=searchCondition 行1 (3列比較) | searchForm.projectName='プロジェクト００１' | searchForm.projectName='プロジェクト００１' | OK |
| 〃 | 〃 | EXPECTED_TABLE[1]=PROJECT 行1 (15列比較) | PROJECT_NAME='プロジェクト００１' | PROJECT_NAME='プロジェクト００１' | OK |
| action/ProjectActionRequestTest/downloadNormal.yaml | 3（全7行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=searchCondition 行1 (3列比較) | searchForm.projectName='プロジェクト００１' | searchForm.projectName='プロジェクト００１' | OK |
| 〃 | 〃 | EXPECTED_VARIABLE=./tmp/html_dump/ProjectActionRequestTest/downloadNormal_Shot1_プロジェクト一覧ダウンロード_プロジェクト一覧.csv (data) 行1 (14列比較) | projectName='"プロジェクト００１"' | projectName='プロジェクト００１' | 表記差 |
| action/ProjectActionRequestTest/editAbNormal.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectActionRequestTest/editNormal.yaml | 3（全21行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=form1 行8 (2列比較) | name='clientName' | name='clientName' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectActionRequestTest/indexNormal.yaml | 3（全30行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=project1_1 行1 (2列比較) | name='projectName' | name='projectName' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (15列比較) | PROJECT_NAME='プロジェクト００１' | PROJECT_NAME='プロジェクト００１' | OK |
| action/ProjectActionRequestTest/listAbNormal.yaml | 3（全5行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (15列比較) | PROJECT_NAME='プロジェクト００１' | PROJECT_NAME='プロジェクト００１' | OK |
| action/ProjectActionRequestTest/listNormal.yaml | 3（全31行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=searchForm1 行12 (2列比較) | name='pageNumber' | name='pageNumber' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (15列比較) | PROJECT_NAME='プロジェクト００１' | PROJECT_NAME='プロジェクト００１' | OK |
| action/ProjectActionRequestTest/newEntityNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=userContext 行1 (3列比較) | REQUEST_ID='newEntity' | REQUEST_ID='newEntity' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectActionRequestTest/setUpDb.yaml | 3（全4行中） | SETUP_TABLE=SYSTEM_ACCOUNT 行1 (11列比較) | USER_ID='105' | USER_ID='105' | OK |
| 〃 | 〃 | SETUP_TABLE=INDUSTRY 行1 (2列比較) | INDUSTRY_CODE='01' | INDUSTRY_CODE='01' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行1 (3列比較) | CLIENT_ID='1' | CLIENT_ID='1' | OK |
| action/ProjectActionRequestTest/showAbNormal.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectActionRequestTest/showNormal.yaml | 3（全21行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=projectDto1 行8 (2列比較) | name='clientName' | name='clientName' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectActionRequestTest/updateAbNormal.yaml | 3（全9行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行2 (1列比較) | [no]='2' | [no]='2' | OK |
| 〃 | 〃 | EXPECTED_TABLE[2]=PROJECT 行1 (16列比較) | PROJECT_ID='99998' | PROJECT_ID='99998' | OK |
| action/ProjectActionRequestTest/updateNormal.yaml | 3（全5行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | EXPECTED_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='99998' | PROJECT_ID='99998' | OK |
| action/ProjectBulkActionRequestTest/backToListNormal.yaml | 3（全31行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=projectListDto1_1 行13 (2列比較) | name='allocationOfCorpExpenses' | name='allocationOfCorpExpenses' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行2 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectBulkActionRequestTest/completeOfUpdateNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='completeOfUpdate' | REQUEST_ID='completeOfUpdate' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1' | OK |
| action/ProjectBulkActionRequestTest/confirmOfUpdateAbNormal.yaml | 3（全5行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (6列比較) | bulkForm.projectList[0].projectId='' | bulkForm.projectList[0].projectId='' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行2 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectBulkActionRequestTest/confirmOfUpdateNormal.yaml | 3（全33行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=projectListDtoInSession1_1 行14 (2列比較) | name='version' | name='version' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行2 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectBulkActionRequestTest/indexNormal.yaml | 3（全53行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=projectListDto1_1 行6 (2列比較) | name='projectStartDate' | name='projectStartDate' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行2 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectBulkActionRequestTest/listAbNormal.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| 〃 | 〃 | LIST_MAP=searchCondition 行1 (3列比較) | searchForm.projectName='プロジェクト００１' | searchForm.projectName='プロジェクト００１' | OK |
| action/ProjectBulkActionRequestTest/listNormal.yaml | 3（全62行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=projectListDto1_1 行12 (2列比較) | name='sga' | name='sga' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行2 (16列比較) | PROJECT_ID='99999' | PROJECT_ID='99999' | OK |
| action/ProjectBulkActionRequestTest/setUpDb.yaml | 3（全4行中） | SETUP_TABLE=SYSTEM_ACCOUNT 行1 (11列比較) | USER_ID='105' | USER_ID='105' | OK |
| 〃 | 〃 | SETUP_TABLE=INDUSTRY 行1 (2列比較) | INDUSTRY_CODE='01' | INDUSTRY_CODE='01' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行1 (3列比較) | CLIENT_ID='1' | CLIENT_ID='1' | OK |
| action/ProjectBulkActionRequestTest/updateAbNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=user 行1 (3列比較) | REQUEST_ID='update' | REQUEST_ID='update' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1' | OK |
| action/ProjectBulkActionRequestTest/updateNormal.yaml | 3（全7行中） | LIST_MAP=testShots 行1 (10列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | SETUP_TABLE[1]=PROJECT 行1 (16列比較) | PROJECT_ID='9998' | PROJECT_ID='9998' | OK |
| 〃 | 〃 | EXPECTED_TABLE[1]=PROJECT 行2 (16列比較) | PROJECT_ID='9999' | PROJECT_ID='9999' | OK |
| form/ProjectBulkFormTest/testBeanValidation.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (4列比較) | title='正常系-1' | title='正常系-1' | OK |
| 〃 | 〃 | LIST_MAP=params 行1 (10列比較) | projectList[0].projectId='${半角数字, 9}' | projectList[0].projectId='${半角数字, 9}' | OK |
| 〃 | 〃 | LIST_MAP=params 行2 (10列比較) | projectList[0].projectId='${半角数字, 9}' | projectList[0].projectId='${半角数字, 9}' | OK |
| form/ProjectFormTest/testBeanValidation.yaml | 3（全10行中） | LIST_MAP=testShots 行1 (4列比較) | title='正常系-1' | title='正常系-1' | OK |
| 〃 | 〃 | LIST_MAP=params 行1 (6列比較) | projectName='サンプルプロジェクト' | projectName='サンプルプロジェクト' | OK |
| 〃 | 〃 | LIST_MAP=params 行5 (6列比較) | projectName='サンプルプロジェクト' | projectName='サンプルプロジェクト' | OK |
| form/ProjectFormTest/testCharsetAndLength.yaml | 3（全5行中） | LIST_MAP=charsetAndLength 行1 (20列比較) | propertyName='projectName' | propertyName='projectName' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行3 (20列比較) | propertyName='projectLeader' | propertyName='projectLeader' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行5 (20列比較) | propertyName='note' | propertyName='note' | OK |
| form/ProjectFormTest/testSingleValidation.yaml | 3（全157行中） | LIST_MAP=singleValidation 行1 (7列比較) | propertyName='projectType' | propertyName='projectType' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行79 (7列比較) | propertyName='projectEndDate' | propertyName='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行157 (7列比較) | propertyName='allocationOfCorpExpenses' | propertyName='allocationOfCorpExpenses' | OK |
| form/ProjectSearchFormTest/testBeanValidation.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (4列比較) | title='正常系-1' | title='正常系-1' | OK |
| 〃 | 〃 | LIST_MAP=params 行1 (2列比較) | projectClass[0].projectClass='ss' | projectClass[0].projectClass='ss' | OK |
| 〃 | 〃 | LIST_MAP=params 行2 (2列比較) | projectClass[0].projectClass='hoge' | projectClass[0].projectClass='hoge' | OK |
| form/ProjectSearchFormTest/testCharsetAndLength.yaml | 2（元データ2件のため全件） | LIST_MAP=charsetAndLength 行1 (20列比較) | propertyName='clientName' | propertyName='clientName' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行2 (20列比較) | propertyName='projectName' | propertyName='projectName' | OK |
| form/ProjectSearchFormTest/testSingleValidation.yaml | 3（全155行中） | LIST_MAP=singleValidation 行1 (7列比較) | propertyName='clientId' | propertyName='clientId' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行78 (7列比較) | propertyName='projectEndDateBegin' | propertyName='projectEndDateBegin' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行155 (7列比較) | propertyName='pageNumber' | propertyName='pageNumber' | OK |
| form/ProjectTargetFormTest/testCharsetAndLength.yaml | 1（元データ1件のため全件） | LIST_MAP=charsetAndLength 行1 (19列比較) | propertyName='projectId' | propertyName='projectId' | OK |
| form/ProjectUpdateFormTest/testBeanValidation.yaml | 3（全10行中） | LIST_MAP=testShots 行1 (4列比較) | title='正常系-1' | title='正常系-1' | OK |
| 〃 | 〃 | LIST_MAP=params 行1 (6列比較) | projectName='サンプルプロジェクト' | projectName='サンプルプロジェクト' | OK |
| 〃 | 〃 | LIST_MAP=params 行5 (6列比較) | projectName='サンプルプロジェクト' | projectName='サンプルプロジェクト' | OK |
| form/ProjectUpdateFormTest/testCharsetAndLength.yaml | 3（全5行中） | LIST_MAP=charsetAndLength 行1 (20列比較) | propertyName='projectName' | propertyName='projectName' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行3 (20列比較) | propertyName='projectLeader' | propertyName='projectLeader' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行5 (20列比較) | propertyName='note' | propertyName='note' | OK |
| form/ProjectUpdateFormTest/testSingleValidation.yaml | 3（全157行中） | LIST_MAP=singleValidation 行1 (7列比較) | propertyName='projectType' | propertyName='projectType' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行79 (7列比較) | propertyName='projectEndDate' | propertyName='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行157 (7列比較) | propertyName='allocationOfCorpExpenses' | propertyName='allocationOfCorpExpenses' | OK |
| action/ProjectUploadActionRequestTest/indexForbidden.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=userContext 行1 (3列比較) | REQUEST_ID='index' | REQUEST_ID='index' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectUploadActionRequestTest/indexNormal.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (8列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=userContext 行1 (3列比較) | REQUEST_ID='index' | REQUEST_ID='index' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | [no]='1' | [no]='1.0' | 表記差(マーカー) |
| action/ProjectUploadActionRequestTest/setUpDb.yaml | 3（全6行中） | SETUP_TABLE=SYSTEM_ACCOUNT 行1 (11列比較) | USER_ID='105' | USER_ID='105' | OK |
| 〃 | 〃 | SETUP_TABLE=USERS 行2 (3列比較) | USER_ID='106' | USER_ID='106' | OK |
| 〃 | 〃 | SETUP_TABLE=CLIENT 行1 (3列比較) | CLIENT_ID='1' | CLIENT_ID='1' | OK |
| action/ProjectUploadActionRequestTest/uploadAbNormal.yaml | 3（全9行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=userContext 行1 (3列比較) | REQUEST_ID='upload' | REQUEST_ID='upload' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行4 (1列比較) | uploadFile='${attach:./src/test/resources/data/requestTest/uploadAbNormal_formatError2.csv}' | uploadFile='${attach:./src/test/resources/data/requestTest/uploadAbNormal_formatError2.csv}' | OK |
| action/ProjectUploadActionRequestTest/uploadForbidden.yaml | 3（元データ3件のため全件） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=userContext 行1 (3列比較) | REQUEST_ID='upload' | REQUEST_ID='upload' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | uploadFile='' | uploadFile='' | OK |
| action/ProjectUploadActionRequestTest/uploadNormal.yaml | 3（全4行中） | LIST_MAP=testShots 行1 (9列比較) | no='1' | no='1' | OK |
| 〃 | 〃 | LIST_MAP=requestParams 行1 (1列比較) | uploadFile='${attach:./src/test/resources/data/requestTest/uploadNormal.csv}' | uploadFile='${attach:./src/test/resources/data/requestTest/uploadNormal.csv}' | OK |
| 〃 | 〃 | EXPECTED_TABLE[1]=PROJECT 行1 (15列比較) | PROJECT_NAME='プロジェクト一括登録' | PROJECT_NAME='プロジェクト一括登録' | OK |
| dto/ProjectUploadDtoTest/testBeanValidation.yaml | 3（全10行中） | LIST_MAP=testShots 行1 (4列比較) | title='正常系-1' | title='正常系-1' | OK |
| 〃 | 〃 | LIST_MAP=params 行1 (6列比較) | projectName='サンプルプロジェクト' | projectName='サンプルプロジェクト' | OK |
| 〃 | 〃 | LIST_MAP=params 行5 (6列比較) | projectName='サンプルプロジェクト' | projectName='サンプルプロジェクト' | OK |
| dto/ProjectUploadDtoTest/testCharsetAndLength.yaml | 3（全5行中） | LIST_MAP=charsetAndLength 行1 (21列比較) | propertyName='projectName' | propertyName='projectName' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行3 (21列比較) | propertyName='projectLeader' | propertyName='projectLeader' | OK |
| 〃 | 〃 | LIST_MAP=charsetAndLength 行5 (21列比較) | propertyName='note' | propertyName='note' | OK |
| dto/ProjectUploadDtoTest/testSingleValidation.yaml | 3（全157行中） | LIST_MAP=singleValidation 行1 (3列比較) | propertyName='projectType' | propertyName='projectType' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行79 (3列比較) | propertyName='projectEndDate' | propertyName='projectEndDate' | OK |
| 〃 | 〃 | LIST_MAP=singleValidation 行157 (3列比較) | propertyName='allocationOfCorpExpenses' | propertyName='allocationOfCorpExpenses' | OK |

### 検出した差異（全行比較で検出。サンプル行以外も含む）

| # | 種別 | YAML ファイル | 箇所 | Excel 値（表示値） | YAML 値 | NTF 上の扱い |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | 外側ダブルクォート除去（RAW_DIFF 30セル） | action/ProjectActionRequestTest/downloadNormal.yaml | EXPECTED_VARIABLE（CSV 期待値）header/data 各14セル | `"プロジェクト名"` / `"2000"` 等 | `プロジェクト名` / `2000` 等 | 一致（NTF の Excel 用 `QuotationTrimmer` が外側1層の `"` を除去した値と同じ） |
| 〃 | 〃 | dto/ProjectUploadDtoTest/testSingleValidation.yaml | LIST_MAP=singleValidation 行61・行78 の input1 | `"?"` | `?` | 一致（同上） |
| 2 | マーカー値の数値表記（MARKER_DIFF 29行） | action/*Test/*.yaml（AuthenticationActionRequestTest 3, ProjectActionRequestTest 21, ProjectBulkActionRequestTest 3, ProjectUploadActionRequestTest 2） | LIST_MAP=requestParams のマーカーカラムだけの行（`[no]`） | `1` | `1.0` | 影響なし（マーカーカラムは NTF の `HeaderLine` が除外しテストデータとして使わない。converter は行数保持のためマーカー列だけを出力し、値は NTF 本体と同じ読み取り（`PoiXlsReader` の `cell.toString()`）で数値セルが `1.0` になる） |
| 3 | 数値セルの表記（ERROR 1セル） | action/ProjectActionRequestTest/downloadNormal.yaml | SETUP_TABLE[1]=PROJECT 行1 COST_OF_GOODS_SOLD | `2000`（数値書式セル） | `2000.0` | NTF が Excel を読んだ値と同じ（`PoiXlsReader` が `cell.toString()` で `2000.0` を返す）。H2 の INTEGER カラムへ投入される。過去の移行コミット `f96c145` の YAML も同値で、旧 steering では「H2 が暗黙変換するため影響なし」と判断済みだった |

- ユーザー判断: 上記3種類の差異は一致として扱い、修正せずに次へ進める。
- 参考（差異ではない）: `action/ProjectActionRequestTest/updateNormal.yaml` の LIST_MAP=testShots で Excel ヘッダに `expectedTable` が2列（6列目・10列目）あり、converter が WARN を出力して後方列を採用。全行で両列の値が同一のためデータ欠落なし（NTF も後勝ち）。
- スクリプト補足: PyYAML（YAML 1.1）の真偽値解釈を避けるため全スカラーを文字列として読む（BaseLoader）。マーカーカラムは NTF と同様に除外して比較する。

## .xls/.xlsx 併存チェック（該当時のみ）
- 併存の有無: なし（`.xlsx` 6件・`.xls` 9件、拡張子を除いた同名ファイルなし）
- 差分確認結果の保存先: 該当なし
- 未使用側（通常 `.xlsx`）の削除: 該当なし

## Overall Verdict

- Self-check: OK（表記差3種類はユーザー判断により一致とみなす）
- QA:
- Language expert: N/A
- Software-engineering expert: N/A
- Ready for user review: Yes
