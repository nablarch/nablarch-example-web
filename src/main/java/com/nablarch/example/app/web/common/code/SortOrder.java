package com.nablarch.example.app.web.common.code;

import nablarch.core.validation.ee.EnumElement.WithValue;
/**
 * 並び順を定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum SortOrder implements WithValue<String> {
    /** 昇順 */
    ASC("asc", "昇順"),
    /** 降順 */
    DESC("desc", "降順");

    /** 並び順のラベル */
    private final String label;
    /** 並び順のコード */
    private final String code;

    /**
     * コンストラクタ。
     * @param code コード値
     * @param label ラベル
     */
    SortOrder(String code, String label) {
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
