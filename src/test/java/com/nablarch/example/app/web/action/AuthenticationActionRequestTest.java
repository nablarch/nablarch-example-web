package com.nablarch.example.app.web.action;

import com.nablarch.example.app.test.ExampleHttpRequestTest;
import com.nablarch.example.app.test.ExampleHttpRequestTestSupport;
import com.nablarch.example.app.test.advice.SignedInAdvice;
import com.nablarch.example.app.web.common.authentication.context.LoginUserPrincipal;
import nablarch.common.authorization.role.session.SessionStoreUserRoleResolver;
import nablarch.common.web.session.SessionUtil;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.ExecutionContext;
import nablarch.test.Assertion;
import nablarch.test.core.http.BasicAdvice;
import nablarch.test.core.http.TestCaseInfo;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

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
        support.execute("loginPostNormal", new BasicAdvice() {

            @Override
            public void beforeExecute(TestCaseInfo testCaseInfo, ExecutionContext context) {
                SessionUtil.invalidate(context);
            }

            @Override
            public void afterExecute(TestCaseInfo testCaseInfo,
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
     * 管理者でログインした後、ログアウトせずに一般ユーザーでログインするケース。
     * <p>
     * {@link SignedInAdvice}で管理者としてログイン済みの状態(セッションストアに管理者ロールが保存された状態)を作り、
     * その状態のまま一般ユーザーでログインする。
     * 一般ユーザーでログインした時点で、セッションストアに残っていた管理者ロールが消えていること。
     */
    @Test
    void loginPostAfterAdminLogin() {
        support.execute("loginPostAfterAdminLogin", new SignedInAdvice() {

            @Override
            public void afterExecute(TestCaseInfo testCaseInfo,
                                     ExecutionContext context) {

                LoginUserPrincipal userContext = SessionUtil.orNull(context, "userContext");
                if (userContext == null) {
                    Assertion.fail();
                }

                support.assertEntity(testCaseInfo.getSheetName(),
                        "userContext" + testCaseInfo.getTestCaseNo(), userContext);

                SessionStoreUserRoleResolver resolver = SystemRepository.get("userRoleResolver");
                Collection<String> roles = resolver.resolve(String.valueOf(userContext.getUserId()), context);
                assertThat(roles, empty());
            }
        });
    }

    /**
     * ログイン異常系ケース。
     */
    @Test
    void loginAbNormal() {
        support.execute("loginAbNormal", new BasicAdvice() {

            @Override
            public void afterExecute(TestCaseInfo testCaseInfo,
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
            public void afterExecute(TestCaseInfo testCaseInfo,
                                     ExecutionContext context) {
                if (SessionUtil.orNull(context, "userContext") != null) {
                    Assertion.fail();
                }
            }
        });
    }
}
