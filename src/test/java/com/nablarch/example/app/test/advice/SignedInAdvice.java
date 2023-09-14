package com.nablarch.example.app.test.advice;

import com.nablarch.example.app.web.common.authentication.context.LoginUserPrincipal;
import nablarch.common.authorization.role.session.SessionStoreUserRoleUtil;
import nablarch.common.web.session.SessionUtil;
import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.core.db.statement.SqlResultSet;
import nablarch.core.db.statement.SqlRow;
import nablarch.core.db.transaction.SimpleDbTransactionExecutor;
import nablarch.core.db.transaction.SimpleDbTransactionManager;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.ExecutionContext;
import nablarch.test.core.http.BasicAdvice;
import nablarch.test.core.http.TestCaseInfo;

import java.util.Collections;


/**
 * テストケースの事前準備で毎回ログイン処理を実装しなくて済むように、
 * 自動的にログイン状態になるように設定した {@link BasicAdvice}の拡張クラス。
 *</p>
 * ログインユーザのIDは、デフォルトでは {@code 105} となる。
 * ユーザIDは {@link #setUserId(Integer)} で指定可能。
 *
 * @author Nabu Rakutaro
 */
public class SignedInAdvice extends BasicAdvice {

    /** ログインユーザのID */
    private Integer userId = 105;

    /**
     * ユーザIDを取得する。
     * @return ユーザID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * ユーザIDを設定する。
     * @param userId ユーザID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * {@inheritDoc}
     * ログイン状態にしてからテストケース個別の事前処理を実行する。
     */
    @Override
    public final void beforeExecute(TestCaseInfo testCaseInfo,
                                    ExecutionContext context) {
        setLoginUser(testCaseInfo, context);
        signedInBeforeExecute(testCaseInfo, context);
    }

    /**
     * テストの事前処理を実装する。
     * <p>
     * 本メソッド実行前にログイン処理が自動実行される。
     */
    protected void signedInBeforeExecute(TestCaseInfo testCaseInfo,
                                         ExecutionContext context) {
        // NOP。必要があればケースごとに事前準備を実装する。
    }

    /**
     * ユーザをログイン済みにする。
     *
     * @param context 実行コンテキスト
     */
    private void setLoginUser(TestCaseInfo testCaseInfo, ExecutionContext context) {
        boolean admin = obtainAdminFlag(testCaseInfo);

        LoginUserPrincipal userContext = new LoginUserPrincipal();
        userContext.setUserId(userId);
        userContext.setAdmin(admin);
        SessionUtil.put(context, "userContext", userContext);

        if (admin) {
            SessionStoreUserRoleUtil.save(Collections.singleton(LoginUserPrincipal.ROLE_ADMIN), context);
        }
    }

    /**
     * テストケース情報に設定されたユーザIDが管理者権限を持つかどうかを取得する。
     * @param testCaseInfo テストケース情報
     * @return ユーザIDが管理者権限を持つ場合は {@code true}
     */
    private boolean obtainAdminFlag(TestCaseInfo testCaseInfo) {
        SimpleDbTransactionManager dbManager = SystemRepository.get("defaultDbTransactionManager");
        return new SimpleDbTransactionExecutor<Boolean>(dbManager) {
            @Override
            public Boolean execute(AppDbConnection connection) {
                final SqlPStatement stmt =
                    connection.prepareStatementBySqlId("com.nablarch.example.app.ExampleTest#GET_SYSTEM_ACCOUNT_BY_USER_ID");
                stmt.setString(1, testCaseInfo.getUserId());

                final SqlResultSet result = stmt.retrieve();
                if (result.isEmpty()) {
                    return false;
                } else {
                    final SqlRow row = result.get(0);
                    return row.getBoolean("ADMIN_FLAG");
                }
            }
        }.doTransaction();
    }
}