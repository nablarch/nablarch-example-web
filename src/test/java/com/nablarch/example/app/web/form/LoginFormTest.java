package com.nablarch.example.app.web.form;

import nablarch.test.core.db.EntityTestSupport;
import nablarch.test.junit5.extension.db.EntityTest;
import org.junit.jupiter.api.Test;

@EntityTest
public class LoginFormTest {

    private static final Class<?> targetClass = LoginForm.class;

    EntityTestSupport support;

    @Test
    public void 文字列長と文字種の単項目精査結果が正しいことを検証する() {
        String sheetName = "testCharsetAndLength";
        String id = "charsetAndLength";
        support.testValidateCharsetAndLength(targetClass, sheetName, id);
    }
}