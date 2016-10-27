package com.nablarch.example.app.web.handler;

import nablarch.fw.web.handler.normalizer.Normalizer;

/**
 * 日付のノーマライズを行うノーマライザ実装クラス。
 * <p/>
 * 日付に含まれるスラッシュを除去する。
 *
 * @author Nabu Rakutaro
 */
public class DateNormalizer implements Normalizer {

    /**
     * ノーマライズ対象かどうかを返す。
     *
     * @param key パラメータのキー
     * @return ノーマライズ対象の場合は {@code true}
     */
    @Override
    public boolean canNormalize(String key) {
        return key.toLowerCase().contains("date");
    }

    /**
     * スラッシュを除去した値を返す。
     *
     * @param value スラッシュを除去する値
     * @return スラッシュを除去した値
     */
    @Override
    public String[] normalize(String[] value) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] != null) {
                value[i] = value[i].replace("/", "");
            }
        }
        return value;
    }
}
