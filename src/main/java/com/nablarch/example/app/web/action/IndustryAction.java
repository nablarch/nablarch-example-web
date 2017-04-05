package com.nablarch.example.app.web.action;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nablarch.example.app.web.dao.IndustryDao;
import com.nablarch.example.app.web.dto.IndustryDto;
import nablarch.core.beans.BeanUtil;
import nablarch.integration.doma.DomaDaoRepository;
import nablarch.integration.doma.Transactional;

/**
 * 業種検索API
 *
 * @author Nabu Rakutaro
 */
public class IndustryAction {

    /**
     * 業種一覧を取得する。
     *
     * @return 業種一覧
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<IndustryDto> find() {
        return DomaDaoRepository.get(IndustryDao.class).findAll()
                .stream()
                .map(industry -> BeanUtil.createAndCopy(IndustryDto.class, industry))
                .collect(Collectors.toList());
    }
}
