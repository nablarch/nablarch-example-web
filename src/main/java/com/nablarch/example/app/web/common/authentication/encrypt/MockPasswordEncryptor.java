package com.nablarch.example.app.web.common.authentication.encrypt;

/**
 * テスト用に暗号化しないクラスを作成
 * 
 * @author Yutaka Kanayama
 */
public class MockPasswordEncryptor implements PasswordEncryptor {

    @Override
    public String encrypt(String saltSeed, String password) {
        return password;
    }

}