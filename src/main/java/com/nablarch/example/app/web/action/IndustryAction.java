package com.nablarch.example.app.web.action;

import com.nablarch.example.app.entity.Industry;
import com.nablarch.example.app.web.dto.IndustryDto;
import nablarch.common.dao.UniversalDao;
import nablarch.core.beans.BeanUtil;
import nablarch.core.beans.CopyOptions;
import nablarch.fw.dicontainer.web.RequestScoped;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 業種検索API
 *
 * @author Nabu Rakutaro
 */
@RequestScoped
public class IndustryAction {

    /**
     * 業種一覧を取得する。
     *
     * @return 業種一覧
     */
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndustryDto> find() {
        List<Industry> industries = UniversalDao.findAll(Industry.class);
        CopyOptions options = CopyOptions.options().excludes("clientList").build();
        return industries
                .stream()
                .map(industry -> BeanUtil.createAndCopy(IndustryDto.class, industry, options))
                .collect(Collectors.toList());
    }
}
