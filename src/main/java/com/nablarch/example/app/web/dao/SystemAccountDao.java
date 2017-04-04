package com.nablarch.example.app.web.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import nablarch.integration.doma.DomaConfig;

import com.nablarch.example.app.entity.SystemAccount;

/**
 * システムアカウントに関するDAO。
 *
 * @author Nabu Rakutaro
 */
@Dao(config = DomaConfig.class)
public interface SystemAccountDao {

    /**
     * ログインIDを条件にシステムアカウントを取得する。
     *
     * @param loginId ログインID
     * @return システムアカウント
     */
    @Select
    SystemAccount findByLoginId(String loginId);

    /**
     * ログインIDと有効期限を条件にシステムアカウントを取得する。
     *
     * @param loginId ログインID
     * @param nowDate 現在日付
     * @return システムアカウント
     */
    @Select(ensureResult = true)
    SystemAccount findByLoginIdAndEffectiveDate(String loginId, Date nowDate);

    /**
     * システムアカウントの認証失敗関数を更新する。
     * @param userId ユーザID
     * @param failedCount 失敗回数
     * @param locked ロックするか否か
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateFailedCountAndLocked(Integer userId, Short failedCount, Boolean locked);

    /**
     * 認証失敗回数をリセットする。
     * @param userId ユーザID
     * @param failedCountToLock 認証失敗回数の上限
     * @param nowTime タイムスタンプ
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int resetFailedCount(Integer userId, int failedCountToLock, Timestamp nowTime);
}
