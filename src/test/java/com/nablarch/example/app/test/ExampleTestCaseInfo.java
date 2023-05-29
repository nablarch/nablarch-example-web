package com.nablarch.example.app.test;

import nablarch.test.core.http.TestCaseInfo;

import java.util.List;
import java.util.Map;

/**
 * {@link TestCaseInfo}を拡張したクラス。
 */
public class ExampleTestCaseInfo extends TestCaseInfo {

    public ExampleTestCaseInfo(String sheetName, Map<String, String> testCaseParams, List<Map<String, String>> context, List<Map<String, String>> request, List<Map<String, String>> expectedResponseListMap, List<Map<String, String>> cookie, List<Map<String, String>> queryParams) {
        super(sheetName, testCaseParams, context, request, expectedResponseListMap, cookie, queryParams);
    }
}
