package com.nablarch.example.app.test;

import com.nablarch.example.app.web.handler.SlashNormalizer;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * {@link com.nablarch.example.app.web.handler.SlashNormalizer}のテストクラス。
 */
public class SlashNormalizerTest {

    @Test
    public void testCanNormalize(){
        SlashNormalizer normalizer = new SlashNormalizer();
        //キーに開始日,終了日名が含まれている
        assertTrue(normalizer.canNormalize("form.projectStartDate"));
        assertTrue(normalizer.canNormalize("form.projectEndDate"));
        //キーに開始日,終了日名が含まれていない
        assertFalse(normalizer.canNormalize("form.sample"));
        //キーがnull
        assertFalse(normalizer.canNormalize(null));
    }

    @Test
    public void testNormalize(){
        SlashNormalizer normalizer = new SlashNormalizer();

        String[] multipleElements = new String[]{"2016/01/01", "2016/02/02"};
        //値が置換対象
        assertThat(normalizer.normalize(multipleElements), arrayContaining("20160101", "20160202"));
        //値が置換対象でない
        multipleElements = new String[]{"20160101", "20160202"};
        assertThat(normalizer.normalize(multipleElements), arrayContaining("20160101", "20160202"));
        //配列がnull
        multipleElements = null;
        assertThat(normalizer.normalize(multipleElements), is(nullValue()));
        //値がnull
        multipleElements = new String[]{"2016/01/01", null};
        assertThat(normalizer.normalize(multipleElements), arrayContaining("20160101", null));
        //配列の長さが0
        multipleElements = new String[]{};
        assertThat(normalizer.normalize(multipleElements), emptyArray());
    }
}
