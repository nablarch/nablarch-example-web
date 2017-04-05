package com.nablarch.example.app.web.action;

import com.nablarch.example.app.entity.Client;
import com.nablarch.example.app.entity.Industry;
import com.nablarch.example.app.web.dao.TestDao;
import com.nablarch.example.app.web.dto.ClientDto;
import nablarch.core.message.ApplicationException;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.fw.web.MockHttpRequest;
import nablarch.integration.doma.DomaConfig;
import nablarch.integration.doma.DomaDaoRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * {@link ClientAction}のテストクラス
 */
public class ClientActionTest {

    /** テスト対象 */
    private ClientAction sut = new ClientAction();

    private static LocalTransaction transaction;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

            Client client = new Client();
            client.setClientName("テスト顧客" + i);
            client.setIndustryCode("0" + i);
            DomaDaoRepository.get(TestDao.class).insert(client);
        });
    }

    @After
    public void tearDown() {
        transaction.rollback();
    }

    /**
     * 検索条件に合致する顧客が検索できること
     * @throws Exception
     */
    @Test
    public void testFind() throws Exception {
        MockHttpRequest request = new MockHttpRequest();
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put("industryCode", new String[]{"01"});
        request.setParamMap(paramMap);
        List<ClientDto> result = sut.find(request);

        assertThat(result, contains(allOf(
                hasProperty("clientName", is("テスト顧客1")),
                hasProperty("industryCode", is("01")),
                hasProperty("industryName", is("name1")))));
    }

    /**
     * 検索条件が不正な場合は例外が送出されること
     * @throws Exception
     */
    @Test
    public void testFind_validationError() throws Exception {
        MockHttpRequest request = new MockHttpRequest();
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put("clientName", new String[]{"test"});
        request.setParamMap(paramMap);

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage(startsWith("顧客名は64文字以下の全角文字で入力してください。"));

        sut.find(request);
    }

}