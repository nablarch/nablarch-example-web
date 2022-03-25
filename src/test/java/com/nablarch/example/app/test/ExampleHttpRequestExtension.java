package com.nablarch.example.app.test;

import nablarch.test.event.TestEventDispatcher;
import nablarch.test.junit5.extension.http.BasicHttpRequestTestExtension;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * {@link ExampleHttpRequestTestSupport}をテストで使用できるようにするための拡張。
 * @author Nabu Rakutaro
 */
public class ExampleHttpRequestExtension extends BasicHttpRequestTestExtension {

    @Override
    protected TestEventDispatcher createSupport(Object testInstance, ExtensionContext context) {
        ExampleHttpRequestTest annotation = findAnnotation(testInstance, ExampleHttpRequestTest.class);
        return new ExampleHttpRequestTestSupport(testInstance.getClass(), annotation.baseUri());
    }
}
