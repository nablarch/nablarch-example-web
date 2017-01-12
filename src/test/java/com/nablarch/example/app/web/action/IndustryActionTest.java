package com.nablarch.example.app.web.action;

import com.nablarch.example.app.entity.Client;
import com.nablarch.example.app.entity.Industry;
import com.nablarch.example.app.entity.Project;
import nablarch.common.dao.UniversalDao;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.test.core.db.DbAccessTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * {@link IndustryAction}のテストクラス
 */
public class IndustryActionTest {

    /** テスト対象 */
    private IndustryAction sut = new IndustryAction();

    private DbAccessTestSupport transaction = new DbAccessTestSupport(getClass());

    @Before
    public void setup() {
        transaction.beginTransactions();

        UniversalDao.findAll(Project.class).forEach(UniversalDao::delete);
        UniversalDao.findAll(Client.class).forEach(UniversalDao::delete);
        UniversalDao.findAll(Industry.class).forEach(UniversalDao::delete);

        IntStream.range(0, 3).forEach(i -> {
            Industry entity = new Industry();
            entity.setIndustryCode("0" + i);
            entity.setIndustryName("name" + i);
            UniversalDao.insert(entity);
        });
    }

    @After
    public void tearDown() {
        transaction.endTransactions();
    }

    @Test
    public void find() throws Exception {
        List<Industry> result = sut.find();

        assertThat(result.size(), is(3));
        assertThat(result.get(0).getIndustryCode(), is("00"));
        assertThat(result.get(0).getIndustryName(), is("name0"));
        assertThat(result.get(1).getIndustryCode(), is("01"));
        assertThat(result.get(1).getIndustryName(), is("name1"));
        assertThat(result.get(2).getIndustryCode(), is("02"));
        assertThat(result.get(2).getIndustryName(), is("name2"));
    }

}