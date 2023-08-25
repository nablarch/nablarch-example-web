package com.nablarch.example.app.web.form;

import nablarch.test.core.db.EntityTestSupport;
import nablarch.test.junit5.extension.db.EntityTest;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

@EntityTest
public class ProjectBulkFormTest {

    private static final Class<?> targetClass = ProjectBulkForm.class;

    EntityTestSupport support;

    @Test
    public void 全子フォームが正しいことを検証する() {

        support.testBeanValidation(targetClass, "testBeanValidation");
    }

}