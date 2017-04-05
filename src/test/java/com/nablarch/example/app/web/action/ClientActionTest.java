package com.nablarch.example.app.web.action;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nablarch.example.app.web.dto.ClientDto;
import nablarch.core.message.ApplicationException;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.fw.web.MockHttpRequest;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link ClientAction}のテストクラス
 */
public class ClientActionTest {

    /** テスト対象 */
    private ClientAction sut = new ClientAction();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /** コネクション */
    private static Connection con;

    @BeforeClass
    public static void classSetup() throws SQLException {

        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:nablarch_test;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        con = ds.getConnection();

        Statement statement = con.createStatement();
        try {
            statement.execute("drop table project cascade CONSTRAINTS");
            statement.execute("drop table client cascade CONSTRAINTS");
            statement.execute("drop table industry cascade CONSTRAINTS");
        } catch (SQLException ignored) {
            // NOP
        }

        statement.execute("create table client(" +
                "client_id serial not null," +
                "client_name varchar(128) not null," +
                "industry_code char(2) not null," +
                "primary key (client_id))");

        statement.execute("create table industry(" +
                "industry_code char(2) not null," +
                "industry_name varchar(50)," +
                "primary key (industry_code))");


        statement.execute("insert into industry values('00','name0')");
        statement.execute("insert into industry values('01','name1')");
        statement.execute("insert into industry values('02','name2')");

        statement.execute("insert into client values(1,'テスト顧客0','00')");
        statement.execute("insert into client values(2,'テスト顧客1','01')");
        statement.execute("insert into client values(3,'テスト顧客2','02')");

        statement.close();
        con.commit();

        SystemRepository.load(new DiContainer(new XmlComponentDefinitionLoader("unit-test.xml")));
        SystemRepository.load(() -> {
            final Map<String, Object> result = new HashMap<>();
            result.put("dataSource", ds);
            return result;
        });
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        if (con != null) {
            con.close();
        }
        SystemRepository.clear();
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