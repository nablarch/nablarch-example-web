package com.nablarch.example.app.web.form;

import nablarch.test.core.db.EntityTestSupport;
import org.junit.Test;

public class ProjectUpdateFormTest extends EntityTestSupport {

    private static final Class<?> targetClass = ProjectUpdateForm.class;

    @Test
    public void 文字列長と文字種の単項目精査結果が正しいことを検証する() {
        String sheetName = "testCharsetAndLength";
        String id = "charsetAndLength";
        testValidateCharsetAndLength(targetClass, sheetName, id);
    }

    @Test
    public void 文字列長と文字種以外の単項目精査結果が正しいことを検証する() {
        String sheetName = "testSingleValidation";
        String id = "singleValidation";
        testSingleValidation(targetClass, sheetName, id);
    }

    @Test
    public void 項目間精査結果が正しいことを検証する() {

        testBeanValidation(targetClass, "testBeanValidation");
    }
}