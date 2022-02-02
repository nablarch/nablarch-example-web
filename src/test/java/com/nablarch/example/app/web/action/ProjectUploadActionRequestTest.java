package com.nablarch.example.app.web.action;

import com.nablarch.example.app.test.ExampleHttpRequestTest;
import com.nablarch.example.app.test.ExampleHttpRequestTestSupport;
import com.nablarch.example.app.test.advice.SignedInAdvice;
import org.junit.jupiter.api.Test;

/**
 * {@link ProjectUploadAction} のリクエスト単体テストクラス。
 *
 * @author Nabu Rakutaro
 */
@ExampleHttpRequestTest(baseUri = "/action/projectUpload/")
class ProjectUploadActionRequestTest {
    ExampleHttpRequestTestSupport support;

    /**
     * プロジェクト登録確認画面表示正常系ケース。
     */
    @Test
    void indexNormal() {
        support.execute("indexNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト一括登録正常系ケース。
     */
    @Test
    void uploadNormal() {
        support.execute("uploadNormal", new SignedInAdvice());
    }

    /**
     * プロジェクト一括登録異常系ケース。
     */
    @Test
    void uploadAbNormal() {
        support.execute("uploadAbNormal", new SignedInAdvice());
    }

}
