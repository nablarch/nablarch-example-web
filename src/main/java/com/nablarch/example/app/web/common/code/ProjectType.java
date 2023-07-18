package com.nablarch.example.app.web.common.code;

import nablarch.core.validation.ee.EnumElement.WithValue;
/**
 * プロジェクト種別を定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum ProjectType implements WithValue<String> {
    /** 新規開発PJ */
    DEVELOPMENT("development", "新規開発PJ"),
    /** 保守PJ */
    MAINTENANCE("maintenance", "保守PJ");

    /** プロジェクト種別のラベル */
    private final String label;
    /** プロジェクト種別のコード */
    private final String code;

    /**
     * コンストラクタ。
     * @param code コード値
     * @param label ラベル
     */
    ProjectType(String code, String label) {
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
