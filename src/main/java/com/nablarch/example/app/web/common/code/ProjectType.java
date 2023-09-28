package com.nablarch.example.app.web.common.code;

/**
 * プロジェクト種別を定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum ProjectType implements CodeEnum {
    /** 新規開発PJ */
    DEVELOPMENT("development", "新規開発PJ"),
    /** 保守PJ */
    MAINTENANCE("maintenance", "保守PJ");

    /** プロジェクト種別のラベル */
    private final String label;
    /** プロジェクト種別のコード */
    private final String value;

    /**
     * コンストラクタ。
     * @param value コード値
     * @param label ラベル
     */
    ProjectType(String value, String label) {
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
