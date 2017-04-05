package com.nablarch.example.app.web.action;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import com.nablarch.example.app.entity.Industry;
import com.nablarch.example.app.web.dao.TestDao;
import com.nablarch.example.app.web.dto.IndustryDto;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.integration.doma.DomaConfig;
import nablarch.integration.doma.DomaDaoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;

/**
 * {@link IndustryAction}のテストクラス
 */
public class IndustryActionTest {

    /** テスト対象 */
    private IndustryAction sut = new IndustryAction();

    private static LocalTransaction transaction;

    @BeforeClass
    public static void setupClass() {
        SystemRepository.load(new DiContainer(new XmlComponentDefinitionLoader("unit-test.xml")));
        transaction = ((LocalTransactionDataSource) DomaConfig.singleton().getDataSource()).getLocalTransaction(new UtilLoggingJdbcLogger());
    }

    @Before
    public void setup() {
        transaction.begin();

        DomaDaoRepository.get(TestDao.class).deleteAllProject();
        DomaDaoRepository.get(TestDao.class).deleteAllClient();
        DomaDaoRepository.get(TestDao.class).deleteAllIndustry();

        IntStream.range(0, 3).forEach(i -> {
            Industry entity = new Industry();
            entity.setIndustryCode("0" + i);
            entity.setIndustryName("name" + i);
            DomaDaoRepository.get(TestDao.class).insert(entity);
        });
    }

    @After
    public void tearDown() {
        transaction.rollback();
    }

    @Test
    public void find() throws Exception {
        List<IndustryDto> result = sut.find();

        assertThat(result, contains(
                allOf(
                        hasProperty("industryCode", is("00")),
                        hasProperty("industryName", is("name0"))),
                allOf(
                        hasProperty("industryCode", is("01")),
                        hasProperty("industryName", is("name1"))),
                allOf(
                        hasProperty("industryCode", is("02")),
                        hasProperty("industryName", is("name2")))));
    }

}