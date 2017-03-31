package com.nablarch.example.app.web.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.NoResultException;

import nablarch.integration.doma.DomaConfig;

import com.nablarch.example.app.entity.Client;

/**
 * 顧客Dao。
 *
 * @author Nabu Rakutaro
 */
@Dao(config = DomaConfig.class)
public interface ClientDao {

    /**
     * IDを元に顧客情報を検索する。
     *
     * @param id ID
     * @return 顧客情報
     */
    @Select(ensureResult = true)
    Client findById(Integer id);

    /**
     * 顧客が存在しているかどうか。
     *
     * @param id ID
     * @return 存在している場合は{@code true}
     */
    default boolean exists(Integer id) {
        try {
            findById(id);
            return true;
        } catch (NoResultException ignore) {
            return false;
        }
    }

    /**
     * 顧客名称を取得する。
     *
     * @param id ID
     * @return 顧客名称
     */
    default String getName(Integer id) {
        return findById(id).getClientName();
    }
}
