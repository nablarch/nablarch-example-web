package com.nablarch.example.app.web.token;

import jakarta.servlet.http.Cookie;
import nablarch.common.web.token.TokenManager;
import nablarch.fw.web.servlet.NablarchHttpServletRequestWrapper;
import nablarch.fw.web.servlet.ServletExecutionContext;
import nablarch.integration.redisstore.lettuce.LettuceRedisClient;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * redisを使った{@link TokenManager}実装クラス。
 *
 */
public class RedisTokenManager implements TokenManager {

    /** 接続先のRedisの構成ごとに用意された専用のクライアントクラス。
     * どのクライアントクラスを使用するかは、環境設定値 nablarch.lettuce.clientType で設定されており、
     * デフォルト値はredisstore-lettuce.config でLettuceSimpleRedisClientが呼ばれるようになっている。
     * 詳しくは以下の解説書を参考にすること
     * @see <a href="https://nablarch.github.io/docs/6u2/doc/application_framework/adaptors/lettuce_adaptor/redisstore_lettuce_adaptor.html#redisstore-redis-client-config-client-classes">Redisストア(Lettuce)アダプタ - 構成ごとに用意されたクライアントクラス</a>
     */
    private LettuceRedisClient redisClient;

    /** 有効期間(ミリ秒) */
    private Long expiresMilliSeconds;

    /** セッションID */
    private String sessionId;

    /**
     * セッションIDを保持するクッキー名
     * */
    private String cookieName = "NABLARCH_SID";

    /**
     * セッションIDを元に、 Redis に格納するときに使用するキーを作成する。
     * @param sessionId セッションID
     * @return Redis への格納に使用するキー
     */
    public static String toSessionStoreKey(String sessionId) {
        return "nablarch.double.submission." + sessionId;
    }

    @Override
    public void saveToken(String serverToken, NablarchHttpServletRequestWrapper request) {
        // セッションIDを取得する
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        // severTokenを保存する
        redisClient.set(toSessionStoreKey(sessionId), serverToken.getBytes(StandardCharsets.UTF_8));
        // 有効期限を設定する
        // 有効期限はコンポーネント tokenManager のプロパティ expires で設定された値を使用する。
        redisClient.pexpire(toSessionStoreKey(sessionId), expiresMilliSeconds);
    }

    @Override
    public boolean isValidToken(String clientToken, ServletExecutionContext context) {
        // セッションIDを取得する
        final Cookie[] cookies = context.getServletRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        String serverToken;
        // サーバートークンが有効期限切れなどで存在しない場合は無効とする。
        try {
            serverToken = new String(redisClient.get(toSessionStoreKey(sessionId)), StandardCharsets.UTF_8);
        }catch (NullPointerException e){
            return false;
        }

        redisClient.del(toSessionStoreKey(sessionId));
        return clientToken.equals(serverToken);
    }

    @Override
    public void initialize() {
        //何もしない
    }

    /**
     * セッションIDを保持するクッキーの名称を設定する。
     * デフォルトは "NABLARCH_SID"
     *
     * @param cookieName クッキー名
     */
    public void setCookieName(final String cookieName) {
        this.cookieName = cookieName;
    }

    /**
     * {@link LettuceRedisClient} を設定する。
     * @param client {@link LettuceRedisClient}
     */
    public void setClient(LettuceRedisClient client) {
        this.redisClient = client;
    }

    /**
     * 有効期限(単位:秒)を設定する。
     *
     * @param expires 有効期限(単位:秒)
     */
    public void setExpiresSeconds(Long expires) {
        this.expiresMilliSeconds = TimeUnit.SECONDS.toMillis(expires);
    }

}
