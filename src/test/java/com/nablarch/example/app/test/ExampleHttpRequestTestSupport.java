package com.nablarch.example.app.test;

import nablarch.fw.ExecutionContext;
import nablarch.test.core.http.AbstractHttpRequestTestTemplate;
import nablarch.test.core.http.TestCaseInfo;

import java.util.List;
import java.util.Map;

/**
 * web-example向けのリクエスト単体テストサポートクラス。
 *
 * @author Nabu Rakutaro
 */
public class ExampleHttpRequestTestSupport extends AbstractHttpRequestTestTemplate<ExampleTestCaseInfo> {

    private final String baseUri;

    public ExampleHttpRequestTestSupport(Class<?> testClass, String baseUri) {
        super(testClass);
        this.baseUri = baseUri;
    }

    @Override
    protected String getBaseUri() {
        return baseUri;
    }

    /**
     * リクエストスコープに格納されたBeanのリストをアサートする。
     * <p>
     * リクエストスコープからアサート対象のリストオブジェクトを取得し、
     * {@link #assertBeanList(String, String, TestCaseInfo, List)} を実行する。
     *
     * @param sheetName            シート名
     * @param prefix               Excelファイルに定義されたcase番号
     * @param requestScopedVarName リクエストスコープに格納されている変数名
     * @param testCaseInfo         テストケース情報
     * @param context              実行コンテキスト
     */
    public void assertBeanList(String sheetName, String prefix, String requestScopedVarName,
                                  TestCaseInfo testCaseInfo, ExecutionContext context) {

        List<Object> actualList = context.getRequestScopedVar(requestScopedVarName);
        assertBeanList(sheetName, prefix, testCaseInfo, actualList);
    }

    /**
     * Beanのリストをアサートする。
     * </p>
     * 検証対象のリスト型プロパティを取得し、{@param actualList} として引き渡すことで、
     * Bean内のリスト型プロパティを検証することができる。
     * </p>
     * 以下の形式に沿った期待値データがExcelに記述されている必要がある。
     * 「LIST_MAP=prefix + caseNo + "_" + index」<br>
     * 例：projectを期待値とするケース1番の一行目のデータの場合は "LIST_MAP=project1_1" となる。
     *
     * @param sheetName    シート名
     * @param prefix       Excelファイルに定義されたcase番号
     * @param testCaseInfo テストケース情報
     * @param actualList   実際に取得したリスト
     */
    public void assertBeanList(String sheetName, String prefix,
                                  TestCaseInfo testCaseInfo, List<Object> actualList) {

        String expectedName = prefix + testCaseInfo.getTestCaseNo();
        int index = 0;
        for (Object actual : actualList) {
            index++;
            assertEntity(sheetName, expectedName + "_" + index, actual);
        }
    }

    @Override
    protected ExampleTestCaseInfo createTestCaseInfo(String sheetName, Map<String, String> testCaseParams, List<Map<String, String>> contexts, List<Map<String, String>> requests, List<Map<String, String>> expectedResponses, List<Map<String, String>> cookie) {
        return createTestCaseInfo(sheetName, testCaseParams, contexts, requests, expectedResponses, cookie, null);
    }

    @Override
    protected ExampleTestCaseInfo createTestCaseInfo(String sheetName, Map<String, String> testCaseParams, List<Map<String, String>> contexts, List<Map<String, String>> requests, List<Map<String, String>> expectedResponses, List<Map<String, String>> cookie, List<Map<String, String>> queryParams) {
        return new ExampleTestCaseInfo(sheetName, testCaseParams, contexts, requests, expectedResponses, cookie, queryParams);
    }
}
