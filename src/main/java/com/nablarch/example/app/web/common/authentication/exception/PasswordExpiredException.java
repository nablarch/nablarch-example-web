package com.nablarch.example.app.web.common.authentication.exception;

import java.util.Date;


/**
 * ユーザの認証時にパスワードの有効期限が切れている場合に発生する例外。
 * <p/>
 * 対象ユーザのユーザID、パスワード有効期限とチェックに使用した現在日付を保持する。
 * @author Nabu Rakutaro
 */
public class PasswordExpiredException extends AuthenticationException {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** ユーザID */
    private final String userId;

    /** パスワード有効期限 */
    private final Date passwordExpirationDate;

    /** 現在日付 */
    private final Date sysDate;

    /**
     * コンストラクタ。
     * @param userId ユーザID
     * @param passwordExpirationDate パスワードの有効期限
     * @param sysDate 現在日付
     */
    public PasswordExpiredException(String userId, Date passwordExpirationDate, Date sysDate) {
        this.userId = userId;
        this.sysDate = sysDate;
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
    public Date getPasswordExpirationDate() {
        return passwordExpirationDate;
    }

    /**
     * 現在日付を取得する。
     * @return 現在日付
     */
    public Date getSysDate() {
        return sysDate;
    }
}
