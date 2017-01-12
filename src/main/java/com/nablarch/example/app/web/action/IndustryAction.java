package com.nablarch.example.app.web.action;

import com.nablarch.example.app.entity.Industry;
import nablarch.common.dao.UniversalDao;
import nablarch.core.util.annotation.Published;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 業種検索API
 *
 * @author Nabu Rakutaro
 */
@Published
public class IndustryAction {

    /**
     * 業種一覧を取得する。
     *
     * @return 業種一覧
     */
    @Produces(MediaType.APPLICATION_JSON)
    public List<Industry> find() {
        return UniversalDao.findAll(Industry.class);
    }
}
