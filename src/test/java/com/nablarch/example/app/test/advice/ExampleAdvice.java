package com.nablarch.example.app.test.advice;

import nablarch.fw.ExecutionContext;
import nablarch.test.core.http.Advice;
import nablarch.test.core.http.TestCaseInfo;

/**
 * Example用の{@link Advice}実装クラス。
 *
 * @author Nabu Rakutaro
 */
public class ExampleAdvice implements Advice<TestCaseInfo> {

    @Override
    public void beforeExecute(TestCaseInfo exampleTestCaseInfo, ExecutionContext executionContext) {

    }

    @Override
    public void afterExecute(TestCaseInfo exampleTestCaseInfo, ExecutionContext executionContext) {

    }
}
