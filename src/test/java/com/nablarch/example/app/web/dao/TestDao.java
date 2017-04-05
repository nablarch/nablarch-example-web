package com.nablarch.example.app.web.dao;

import com.nablarch.example.app.entity.Client;
import com.nablarch.example.app.entity.Industry;
import nablarch.integration.doma.DomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;

/**
 * テストでテーブルを操作する際に使用するDAO。
 */
@Dao(config = DomaConfig.class)
public interface TestDao {

    @Insert
    int insert(Client entity);

    @Insert
    int insert(Industry entity);

    @Delete(sqlFile = true)
    int deleteAllProject();

    @Delete(sqlFile = true)
    int deleteAllIndustry();

    @Delete(sqlFile = true)
    int deleteAllClient();
}
