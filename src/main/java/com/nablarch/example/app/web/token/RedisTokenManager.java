package com.nablarch.example.app.web.token;

import nablarch.common.web.token.TokenManager;
import nablarch.fw.ExecutionContext;
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
    private LettuceRedisClient client;

    /** 有効期間(ミリ秒) */
    private Long expiresMilliSeconds;

    /** Redis に格納するときに使用するキー */
    private static final String toSessionStoreKey = ExecutionContext.FW_PREFIX  + "double_submission_token";

    @Override
    public void saveToken(String serverToken, NablarchHttpServletRequestWrapper request) {
        // severTokenを保存する
        client.set(toSessionStoreKey, serverToken.getBytes(StandardCharsets.UTF_8));
        // 有効期限を設定する
        // 有効期限はコンポーネント tokenManager のプロパティ expires で設定された値を使用する。
        client.pexpire(toSessionStoreKey, getExpiresMilliSeconds());
    }

    @Override
    public boolean isValidToken(String clientToken, ServletExecutionContext context) {
        String serverToken;
        // サーバートークンが有効期限切れなどで存在しない場合は無効とする。
        try {
            serverToken = new String(client.get(toSessionStoreKey), StandardCharsets.UTF_8);
        }catch (NullPointerException e){
            return false;
        }

        client.del(toSessionStoreKey);
        return clientToken.equals(serverToken);
    }

    @Override
    public void initialize() {
        //何もしない
    }

    /**
     * {@link LettuceRedisClient} を設定する。
     * @param client {@link LettuceRedisClient}
     */
    public void setClient(LettuceRedisClient client) {
        this.client = client;
    }

    /**
     * 有効期限(単位:秒)を設定する。
     *
     * @param expires 有効期限(単位:秒)
     */
    public void setExpires(Long expires) {
        setExpires(expires, TimeUnit.SECONDS);
    }

    /**
     * 有効期限を設定する。
     *
     * @param expires 有効期限
     * @param timeUnit 時間単位
     */
    public void setExpires(Long expires, TimeUnit timeUnit) {
        this.expiresMilliSeconds = timeUnit.toMillis(expires);
    }

    /**
     * 有効期限(単位:ミリ秒)で取得する。
     *
     * @return 有効期限(単位:ミリ秒)
     */
    public long getExpiresMilliSeconds() {
        return expiresMilliSeconds;
    }

}
