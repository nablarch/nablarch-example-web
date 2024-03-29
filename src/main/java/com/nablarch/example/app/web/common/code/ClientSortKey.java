package com.nablarch.example.app.web.common.code;

/**
 * 顧客を定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum ClientSortKey implements CodeEnum {
    /** 顧客ID */
    ID("id", "顧客ＩＤ"),
    /** 顧客名 */
    NAME("name", "顧客名");

    /** 顧客のラベル */
    private final String label;
    /** 顧客のコード */
    private final String value;

    /**
     * コンストラクタ。
     * @param value コード値
     * @param label ラベル
     */
    ClientSortKey(String value, String label) {
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
