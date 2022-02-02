package com.nablarch.example.app.web.action;

import com.nablarch.example.app.test.ExampleHttpRequestTest;
import com.nablarch.example.app.test.ExampleHttpRequestTestSupport;
import com.nablarch.example.app.test.ExampleTestCaseInfo;
import com.nablarch.example.app.test.advice.ExampleAdvice;
import com.nablarch.example.app.test.advice.SignedInAdvice;
import com.nablarch.example.app.web.common.authentication.context.LoginUserPrincipal;
import nablarch.common.web.session.SessionUtil;
import nablarch.fw.ExecutionContext;
import nablarch.test.Assertion;
import org.junit.jupiter.api.Test;

/**
 * {@link AuthenticationAction}のリクエスト単体テストクラス。
 *
 * @author Nabu Rakutaro
 */
@ExampleHttpRequestTest(baseUri = "/action/")
class AuthenticationActionRequestTest {

    ExampleHttpRequestTestSupport support;

    /**
     * ログイン画面表示正常系ケース。
     */
    @Test
    void indexNormal() {
        support.execute("indexNormal");
    }

    /**
     * "/action/login"をRUIとするGETリクエストでログイン画面を表示する正常系ケース。
     */
    @Test
    void loginGetNormal() {
        support.execute("loginGetNormal");
    }

    /**
     * ログイン正常系ケース(POSTリクエスト)。
     */
    @Test
    void loginPostNormal() {
        support.execute("loginPostNormal", new ExampleAdvice() {

            @Override
            public void beforeExecute(ExampleTestCaseInfo testCaseInfo, ExecutionContext context) {
                SessionUtil.invalidate(context);
            }

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {

                LoginUserPrincipal userContext = SessionUtil.orNull(context, "userContext");
                if (userContext == null) {
                    Assertion.fail();
                }

                support.assertEntity(testCaseInfo.getSheetName(),
                        "userContext" + testCaseInfo.getTestCaseNo(), userContext);
            }
        });
    }

    /**
     * ログイン異常系ケース。
     */
    @Test
    void loginAbNormal() {
        support.execute("loginAbNormal", new ExampleAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                if (SessionUtil.orNull(context, "userContext") != null) {
                    Assertion.fail();
                }
            }
        });
    }

    /**
     * ログアウト正常系ケース。
     */
    @Test
    void logoutNormal() {
        support.execute("logoutNormal", new SignedInAdvice() {

            @Override
            public void afterExecute(ExampleTestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                if (SessionUtil.orNull(context, "userContext") != null) {
                    Assertion.fail();
                }
            }
        });
    }
}
