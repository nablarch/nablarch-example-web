package com.nablarch.example.app.web.common.code;

import nablarch.core.validation.ee.EnumElement.WithValue;
/**
 * 顧客を定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum ClientSortKey implements WithValue<String> {
    /** 顧客ID */
    ID("id", "顧客ＩＤ"),
    /** 顧客名 */
    NAME("name", "顧客名");

    /** 顧客のラベル */
    private final String label;
    /** 顧客のコード */
    private final String code;

    /**
     * コンストラクタ。
     * @param code コード値
     * @param label ラベル
     */
    ClientSortKey(String code, String label) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return code;
    }
}
