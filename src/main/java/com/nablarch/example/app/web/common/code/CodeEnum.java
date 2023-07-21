package com.nablarch.example.app.web.common.code;

import nablarch.core.validation.ee.EnumElement.WithValue;
/**
 * コード値を定義したEnumが実装するインタフェース。
 * 
 * @author Nabu Rakutaro
 */
public interface CodeEnum extends WithValue<String> {
    /**
     * ラベルを返却する。
     * @return ラベル
     */
    String getLabel();

    /**
     * コード値を返却する。
     * コード値はバリデーション時に許容する列挙値としても使用する。
     * @return コード値
     */
    @Override // override for Javadoc
    String getValue();
}
