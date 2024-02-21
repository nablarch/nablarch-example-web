package com.nablarch.example.app.web.action;

import com.nablarch.example.app.web.dto.ClientDto;
import com.nablarch.example.app.web.dto.ClientSearchDto;
import com.nablarch.example.app.web.form.ClientSearchForm;
import nablarch.common.dao.UniversalDao;
import nablarch.common.util.WebRequestUtil;
import nablarch.core.beans.BeanUtil;
import nablarch.fw.web.HttpRequest;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 顧客検索API
 *
 * @author Nabu Rakutaro
 */
public class ClientAction {

    /**
     * 指定された条件に合致する顧客を検索する。
     *
     * @param req HTTPリクエスト
     * @return 顧客情報リスト
     */
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClientDto> find(HttpRequest req) {

        // リクエストパラメータをBeanに変換し、BeanValidation実行
        final ClientSearchForm form = WebRequestUtil.getValidatedBean(ClientSearchForm.class, req);

        final ClientSearchDto condition = BeanUtil.createAndCopy(ClientSearchDto.class, form);
        return UniversalDao.findAllBySqlFile(ClientDto.class, "SEARCH_CLIENT", condition);
    }
}
