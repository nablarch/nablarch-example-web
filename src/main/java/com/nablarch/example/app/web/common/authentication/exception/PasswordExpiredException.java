package com.nablarch.example.app.web.common.authentication.exception;

import java.time.LocalDate;


/**
 * ユーザの認証時にパスワードの有効期限が切れている場合に発生する例外。
 * <p/>
 * 対象ユーザのユーザID、パスワード有効期限とチェックに使用した業務日付を保持する。
 * @author Nabu Rakutaro
 */
public class PasswordExpiredException extends AuthenticationException {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** ユーザID */
    private final String userId;

    /** パスワード有効期限 */
    private final LocalDate passwordExpirationDate;

    /** 業務日付 */
    private final LocalDate businessDate;

    /**
     * コンストラクタ。
     * @param userId ユーザID
     * @param passwordExpirationDate パスワードの有効期限
     * @param businessDate 業務日付
     */
    public PasswordExpiredException(String userId, LocalDate passwordExpirationDate, LocalDate businessDate) {
        this.userId = userId;
        this.businessDate = businessDate;
        this.passwordExpirationDate = passwordExpirationDate;
    }

    /**
     * ユーザIDを取得する。
     * @return ユーザID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * パスワード有効期限を取得する。
     * @return パスワード有効期限
     */
    public LocalDate getPasswordExpirationDate() {
        return passwordExpirationDate;
    }

    /**
     * 業務日付を取得する。
     * @return 業務日付
     */
    public LocalDate getBusinessDate() {
        return businessDate;
    }
}
