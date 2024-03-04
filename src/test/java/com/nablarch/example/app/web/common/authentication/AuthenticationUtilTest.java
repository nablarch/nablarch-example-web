package com.nablarch.example.app.web.common.authentication;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.nablarch.example.app.web.common.authentication.encrypt.PasswordEncryptor;
import com.nablarch.example.app.web.common.authentication.exception.AuthenticationException;

import nablarch.core.message.ApplicationException;
import nablarch.core.message.Message;
import nablarch.core.repository.SystemRepository;
import nablarch.core.validation.ValidationResultMessage;
import nablarch.core.validation.ee.Required;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.MockHttpRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static nablarch.test.Assertion.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * {@link AuthenticationUtil}のテストクラス
 *
 * @author Nabu Rakutaro
 */
class AuthenticationUtilTest {

    /**
     * テスト開始時点の {@link PasswordAuthenticator}
     */
    private static PasswordAuthenticator defaultAuthenticator;

    /**
     * テスト開始時点の {@link PasswordEncryptor}
     */
    private static PasswordEncryptor defaultPasswordEncryptor;

    /**
     * {@code authenticator} と {@code passwordEncryptor} を {@link SystemRepository} の
     * "authenticator", "passwordEncryptor" に登録する。
     *
     * @param authenticator {@link SystemRepository} に登録する {@link PasswordAuthenticator}
     * @param encryptor {@link SystemRepository} に登録する {@link PasswordEncryptor}
     */
    private static void setupAuthenticationComponents(final PasswordAuthenticator authenticator, final PasswordEncryptor encryptor) {
        SystemRepository.load(() -> new HashMap<String, Object>() {
            {
                put("authenticator", authenticator);
                put("passwordEncryptor", encryptor);
            }
        });
    }

    /**
     * Delegateを確認するテストのため、 {@link SystemRepository} に、
     * {@code authenticator} として {@link MockAuthenticator} を、
     * {@code passwordEncryptor} として {@link MockPasswordEncryptor} を登録する。
     */
    @BeforeAll
    static void setupRepository() {
        defaultAuthenticator = SystemRepository.get("authenticator");
        defaultPasswordEncryptor = SystemRepository.get("passwordEncryptor");

        setupAuthenticationComponents(new MockAuthenticator(), new MockPasswordEncryptor());
    }

    /**
     * テスト終了時に、 {@link SystemRepository} に登録されている "authenticator" と "passwordEncryptor" を
     * 元の値に戻す。
     */
    @AfterAll
    static void revertRepository() {
        if (defaultAuthenticator != null && defaultPasswordEncryptor != null) {
            setupAuthenticationComponents(defaultAuthenticator, defaultPasswordEncryptor);
        }
    }

    /**
     * {@link AuthenticationUtil#encryptPassword(String, String)}のテスト
     */
    @Test
    void testEncryptPassword() {
        MockPasswordEncryptor sut = SystemRepository.get("passwordEncryptor");
        sut.called(1);
        sut.calledWith("salt", "password");
        AuthenticationUtil.encryptPassword("salt", "password");
        sut.verify("1度だけ呼び出され、引数がそのままpasswordEncryptorに渡されていること。");
    }

    /**
     * {@link AuthenticationUtil#authenticate(String, String)}のテスト
     */
    @Test
    void testAuthenticate() {
        MockAuthenticator sut = SystemRepository.get("authenticator");
        sut.called(1);
        sut.calledWith("userId", "password");
        AuthenticationUtil.authenticate("userId", "password");
        sut.verify("1度だけ呼び出され、引数がそのままauthenticatorに渡されていること。");
    }

    /**
     * テスト用の {@link PasswordAuthenticator}
     * <p/>
     * 呼び出された回数と、引数として渡されたユーザID・パスワードを検証する。
     *
     * @author Nabu Rakutaro
     * @version 1.4
     */
    private static class MockAuthenticator extends MockSupport implements PasswordAuthenticator {

        @Override
        public void authenticate(String userId, String password) throws AuthenticationException {
            this.actualCalled++;
            this.calledWith.add(Arrays.asList(userId, password));
        }
    }

    /**
     * テスト用の {@link PasswordEncryptor}
     * <p/>
     * 呼び出された回数と、引数として渡されたソルト取得元・パスワードを検証する。
     *
     * @author Nabu Rakutaro
     * @version 1.4
     */
    private static class MockPasswordEncryptor extends MockSupport implements PasswordEncryptor {

        @Override
        public String encrypt(String saltSeed, String password) {
            this.actualCalled++;
            this.calledWith.add(Arrays.asList(saltSeed, password));
            return saltSeed + password;
        }
    }

    /**
     * モッククラス用のサポート。
     * <p/>
     * 呼び出された回数と引数を検証するためのメソッドを定義している。
     *
     * @author Nabu Rakutaro
     * @version 1.4
     */
    private static abstract class MockSupport {

        /**
         * 実際に呼び出された回数
         */
        protected int actualCalled;

        /**
         * 実際の呼び出し時の引数
         * <p/>
         * 複数回呼び出される場合、呼び出された順に格納される。
         */
        protected List<List<String>> calledWith;

        /**
         * 期待する呼び出し回数
         */
        protected int expectedCall;

        /**
         * 呼び出し時の期待する引数
         * <p/>
         * 複数回の呼び出しを期待する場合には、それぞれで期待する引数を呼び出し順に格納する。
         */
        protected List<List<String>> expectedParams;

        /**
         * 期待する呼び出し回数を設定する。
         *
         * @param num 期待する呼び出し回数
         */
        public void called(int num) {
            this.expectedCall = num;
        }

        /**
         * 呼び出し時の期待する引数を追加する。
         *
         * @param params 呼び出し時の期待する引数
         */
        public void calledWith(String... params) {
            this.expectedParams.add(Arrays.asList(params));
        }

        /**
         * 実際の呼び出し回数・引数が、期待値と一致することを検証する。
         *
         * @param msg 検証失敗時のメッセージ
         */
        public void verify(String msg) {
            assertThat(msg + ": 呼び出し回数", actualCalled, is(expectedCall));
            assertThat(msg + ": 呼び出し時の引数", calledWith, is(expectedParams));

            initialize();
        }

        /**
         * 実際の値と、期待値を初期化する。
         */
        void initialize() {
            this.actualCalled = 0;
            this.expectedCall = 0;
            this.calledWith = new ArrayList<>();
            this.expectedParams = new ArrayList<>();
        }

        /**
         * デフォルトコンストラクタ
         */
        public MockSupport() {
            initialize();
        }
    }

    /**
     * コンストラクタのカバレッジ
     *
     * @throws Exception コンストラクタの呼び出しに失敗した場合
     */
    @Test
    void testConstructor() throws Exception {
        Constructor<AuthenticationUtil> constructor = AuthenticationUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * HTTPリクエストからBeanが生成され、バリデーションエラーが発生しないこと
     */
    @Test
    public void testGetValidatedBean_Success () {
        HttpRequest req = new MockHttpRequest()
                .setParam("id", "1")
                .setParam("name", "山田太郎");
        Person person = AuthenticationUtil.getValidatedBean(Person.class, req);
        assertThat(person.getId(), is(1L));
        assertThat(person.getName(), is("山田太郎"));
    }

    /**
     * HTTPリクエストからBeanが生成され、バリデーションエラーが発生すること
     */
    @Test
    public void testGetValidatedBean_FailedValidation () {
        HttpRequest req = new MockHttpRequest().setParam("id", "1");
        try {
            AuthenticationUtil.getValidatedBean(Person.class, req);
            fail("とおらない");
        } catch (ApplicationException e) {
            List<Message> messages = e.getMessages();
            assertThat(messages, hasSize(1));
            ValidationResultMessage message = (ValidationResultMessage) messages.get(0);
            assertThat(message.getPropertyName(), Matchers.is("name"));
            assertThat(message.formatMessage(), Matchers.is("入力してください。"));
        }
    }

    /**
     * テスト用のデータクラス
     */
    public static class Person {

        private Long id;

        private String name;

        @Required
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Required
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
