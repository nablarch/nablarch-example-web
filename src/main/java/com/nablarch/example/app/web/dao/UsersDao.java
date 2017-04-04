package com.nablarch.example.app.web.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

import nablarch.integration.doma.DomaConfig;

import com.nablarch.example.app.entity.Users;

/**
 * ユーザに関するDAO。
 *
 * @author Nabu Rakutaro
 */
@Dao(config = DomaConfig.class)
public interface UsersDao {

    /**
     * ユーザIDを条件にユーザ情報を取得する。
     *
     * @param userId ユーザID
     * @return ユーザ情報
     */
    @Select
    Users findById(Integer userId);
}
