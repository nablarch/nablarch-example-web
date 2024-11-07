package com.nablarch.example.app.web.token;

import nablarch.common.web.token.TokenManager;
import nablarch.fw.web.servlet.NablarchHttpServletRequestWrapper;
import nablarch.fw.web.servlet.ServletExecutionContext;
import nablarch.integration.redisstore.lettuce.LettuceRedisClient;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * redisを使った{@link TokenManager}実装クラス。
 */
public class RedisTokenManager implements TokenManager {

    /**
     * 接続先のRedisの構成ごとに用意された専用のクライアントクラス。
     * <p>
     * どのクライアントクラスを使用するかは、環境設定値 nablarch.lettuce.clientType で設定されており、
     * デフォルト値はredisstore-lettuce.config でLettuceSimpleRedisClientが呼ばれるようになっている。
     * 詳しくは以下の解説書を参考にすること。
     *
     * @see <a href="https://nablarch.github.io/docs/6u2/doc/application_framework/adaptors/lettuce_adaptor/redisstore_lettuce_adaptor.html#redisstore-redis-client-config-client-classes">Redisストア(Lettuce)アダプタ - 構成ごとに用意されたクライアントクラス</a>
     */
    private LettuceRedisClient redisClient;

    /**
     * 有効期間(ミリ秒)
     */
    private Long expiresMilliSeconds;

    /**
     * 二重サブミット用のトークンを Redis に格納するときに使用するキーを作成する。
     *
     * @param redisToken トークン
     * @return Redis への格納に使用するキー
     */
    private static String createKeyFromToken(String redisToken) {
        return "nablarch.double.submission." + redisToken;
    }

    @Override
    public void saveToken(String serverToken, NablarchHttpServletRequestWrapper request) {
        String key = createKeyFromToken(serverToken);

        // トークンそのものを紐づけてキーとして保存する。
        // トークンを保持することが目的なので、値はダミーを設定している。
        // ユーザーごとにトークンを管理したい場合はユーザーに紐づく情報をキーにするよう検討してください。
        redisClient.set(key, "dummy".getBytes(StandardCharsets.UTF_8));

        // 有効期限を設定する。
        // 有効期限はコンポーネント tokenManager のプロパティ expiresSeconds で設定された値を使用する。
        redisClient.pexpire(key, expiresMilliSeconds);
    }

    @Override
    public boolean isValidToken(String clientToken, ServletExecutionContext context) {
        String key = createKeyFromToken(clientToken);

        // クライアントトークンを紐づけたキーで値を取得できれば、トークンが有効であると判断できる。
        // サーバートークンが有効期限切れなどで存在しない場合は無効とする。
        byte[] serverTokenBytes = redisClient.get(key);
        if (serverTokenBytes == null) {
            return false;
        }
        redisClient.del(key);
        return true;
    }

    @Override
    public void initialize() {
        // 何もしない
    }

    /**
     * {@link LettuceRedisClient} を設定する。
     *
     * @param redisClient {@link LettuceRedisClient}
     */
    public void setRedisClient(LettuceRedisClient redisClient) {
        this.redisClient = redisClient;
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
