package com.nablarch.example.app.test;

import com.nablarch.example.app.web.handler.DateNormalizer;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * {@link DateNormalizer}のテストクラス。
 */
public class DateNormalizerTest {

    private final DateNormalizer sut = new DateNormalizer();

    /**
     * {@link DateNormalizer#canNormalize(String)}のテスト。
     * <p/>
     * キーがノーマライズ対象の場合、{@code true}を返すこと。
     */
    @Test
    public void testCanNormalizeWhenKeyIsNormalizationTarget() {
        assertThat(sut.canNormalize("form.projectStartDate"), is(true));
        assertThat(sut.canNormalize("form.projectEndDate"), is(true));
        assertThat(sut.canNormalize("form.date"), is(true));
    }

    /**
     * {@link DateNormalizer#canNormalize(String)}のテスト。
     * <p/>
     * キーがノーマライズ対象外の場合、{@code false}を返すこと。
     */
    @Test
    public void testCanNormalizeWhenKeyIsNotNormalizationTarget() {
        assertThat(sut.canNormalize("form.sample"), is(false));
    }

    /**
     * {@link DateNormalizer#normalize(String[])}のテスト。
     * <p/>
     * 値がノーマライズ対象の場合、スラッシュを除去して返すこと。
     */
    @Test
    public void testNormalizeWhenValueIsNormalizationTarget() {
        String[] array = {"2016/01/01", "2016/02/02"};
        assertThat(sut.normalize(array), arrayContaining("20160101", "20160202"));
    }

    /**
     * {@link DateNormalizer#normalize(String[])}のテスト。
     * <p/>
     * 値がノーマライズ対象外の場合、元の値を返すこと。
     */
    @Test
    public void testNormalizeWhenValueIsNotNormalizationTarget() {
        String[] array = {"20160101", "20160202"};
        assertThat(sut.normalize(array), arrayContaining("20160101", "20160202"));
    }

    /**
     * {@link DateNormalizer#normalize(String[])}のテスト。
     * <p/>
     * 配列にnullの値が入っていた場合、nullの値はそのまま返されること。
     */
    @Test
    public void testNormalizeWhenArrayHasNullValue() {
        String[] array = {"20160101", null, "2016/03/03"};
        assertThat(sut.normalize(array), arrayContaining("20160101", null, "20160303"));
    }

    /**
     * {@link DateNormalizer#normalize(String[])}のテスト。
     * <p/>
     * 配列が空の場合、空の配列が返されること。
     */
    @Test
    public void testNormalizeWhenArrayIsEmpty() {
        assertThat(sut.normalize(new String[0]), emptyArray());
    }
}
