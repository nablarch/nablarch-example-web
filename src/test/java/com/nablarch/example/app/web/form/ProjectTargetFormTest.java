package com.nablarch.example.app.web.form;

import nablarch.test.core.db.EntityTestSupport;
import org.junit.Test;

public class ProjectTargetFormTest extends EntityTestSupport {

    private static final Class<?> targetClass = ProjectTargetForm.class;

    @Test
    public void 文字列長と文字種の単項目精査結果が正しいことを検証する() {
        String sheetName = "testCharsetAndLength";
        String id = "charsetAndLength";
        testValidateCharsetAndLength(targetClass, sheetName, id);
    }

}