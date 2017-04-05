package com.nablarch.example.app.web.dao;

import com.nablarch.example.app.entity.Client;
import com.nablarch.example.app.web.dto.ClientDto;
import com.nablarch.example.app.web.dto.ClientSearchDto;
import nablarch.integration.doma.DomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.NoResultException;

import java.util.List;

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
     * 検索条件を元に顧客情報を検索する。
     *
     * @param dto 検索条件
     * @return 検索結果のリスト
     */
    @Select
    List<ClientDto> searchClient(ClientSearchDto dto);

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
