package com.nablarch.example.app.test;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ExampleHttpRequestExtension}を適用する合成アノテーション。
 * @author Nabu Rakutaro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(ExampleHttpRequestExtension.class)
public @interface ExampleHttpRequestTest {
    /**
     * ベースURI。
     * @return ベースURI
     */
    String baseUri();
}
