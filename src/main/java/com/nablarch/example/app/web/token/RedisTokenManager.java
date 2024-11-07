package com.nablarch.example.app.web.token;

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

    @Override
    public void saveToken(String serverToken, NablarchHttpServletRequestWrapper request) {
        // トークンそのものをキーとして保存する。
        // トークンを保持することが目的なので、値はダミーです。
        // ユーザーごとにトークンを管理したい場合はユーザーに紐づく情報をキーにするよう検討してください。
        redisClient.set(serverToken,"dummy".getBytes(StandardCharsets.UTF_8));

        // 有効期限を設定する
        // 有効期限はコンポーネント tokenManager のプロパティ expires で設定された値を使用する。
        redisClient.pexpire(serverToken, expiresMilliSeconds);
    }

    @Override
    public boolean isValidToken(String clientToken, ServletExecutionContext context) {
        // サーバートークンが有効期限切れなどで存在しない場合は無効とする。
        byte[] serverTokenBytes = redisClient.get(clientToken);
        if(serverTokenBytes == null){
            return false;
        }
        redisClient.del(clientToken);
        return true;
    }

    @Override
    public void initialize() {
        //何もしない
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
