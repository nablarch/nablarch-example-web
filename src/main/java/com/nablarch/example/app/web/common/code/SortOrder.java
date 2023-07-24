package com.nablarch.example.app.web.common.code;

/**
 * 並び順を定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum SortOrder implements CodeEnum {
    /** 昇順 */
    ASC("asc", "昇順"),
    /** 降順 */
    DESC("desc", "降順");

    /** 並び順のラベル */
    private final String label;
    /** 並び順のコード */
    private final String value;

    /**
     * コンストラクタ。
     * @param value コード値
     * @param label ラベル
     */
    SortOrder(String value, String label) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return value;
    }
}
