package com.nablarch.example.app.web.common.code;

/**
 * プロジェクトのソートキーを定義したEnum。
 * 
 * @author Nabu Rakutaro
 */
public enum ProjectSortKey implements CodeEnum {
    /** プロジェクトID */
    ID("id", "プロジェクトＩＤ"),
    /** プロジェクト名 */
    NAME("name", "プロジェクト名"),
    /** プロジェクト開始日 */
    START_DATE("startDate", "プロジェクト開始日"),
    /** プロジェクト終了日 */
    END_DATE("endDate", "プロジェクト終了日");

    /** ソートキーのラベル */
    private final String label;
    /** ソートキーのコード */
    private final String value;

    /**
     * コンストラクタ。
     * @param value コード値
     * @param label ラベル
     */
    ProjectSortKey(String value, String label) {
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
