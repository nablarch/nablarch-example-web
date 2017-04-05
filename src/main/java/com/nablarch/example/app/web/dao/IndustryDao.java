package com.nablarch.example.app.web.dao;

import java.util.List;

import com.nablarch.example.app.entity.Industry;
import nablarch.integration.doma.DomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/**
 * 業種Dao。
 *
 * @author Nabu Rakutaro
 */
@Dao(config = DomaConfig.class)
public interface IndustryDao {

    /**
     * 業種情報を検索する。
     *
     * @return 検索結果のリスト
     */
    @Select
    List<Industry> findAll();
}
