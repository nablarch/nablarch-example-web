package com.nablarch.example.app.web.handler;

import nablarch.fw.web.handler.normalizer.Normalizer;

/**
 * プロジェクト開始日・プロジェクト終了日に含まれているスラッシュをトリムするノーマライザ実装クラス。
 *
 * @author Nabu Rakutaro
 */
public class SlashNormalizer implements Normalizer {

    /**
     * ノーマライズ対象かどうかを返す。
     *
     * @param s リクエストパラメータのキー
     * @return キーがプロジェクト開始日かプロジェクト終了日ならtrue,それ以外はfalse
     */
    @Override
    public boolean canNormalize(String s) {
        if(s == null){
            return false;
        }
        return s.contains("projectStartDate") || s.contains("projectEndDate") ? true : false;
    }

    /**
     * スラッシュを除去した値を返す。
     *
     * @param strings
     * @return スラッシュを除去した値
     */
    @Override
    public String[] normalize(String[] strings) {
        if(strings != null){
            for(int i=0; i<strings.length; i++){
                if(strings[i] != null){
                    strings[i] = strings[i].replace("/", "");
                }
            }
        }
        return strings;
    }
}
