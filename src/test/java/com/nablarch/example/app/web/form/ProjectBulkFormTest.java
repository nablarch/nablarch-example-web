package com.nablarch.example.app.web.form;

import nablarch.test.core.db.EntityTestSupport;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectBulkFormTest extends EntityTestSupport {

    private static final Class<?> targetClass = ProjectBulkForm.class;

    @Test
    public void 全子フォームが正しいことを検証する() {

        testBeanValidation(targetClass, "testBeanValidation");
    }

}