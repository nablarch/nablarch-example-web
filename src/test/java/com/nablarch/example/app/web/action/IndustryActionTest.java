package com.nablarch.example.app.web.action;

import static org.hamcrest.CoreMatchers.is;
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

import com.nablarch.example.app.web.dto.IndustryDto;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link IndustryAction}のテストクラス
 */
public class IndustryActionTest {

    /** テスト対象 */
    private IndustryAction sut = new IndustryAction();

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
            statement.execute("drop table client cascade CONSTRAINTS");
            statement.execute("drop table industry cascade CONSTRAINTS");
        } catch (SQLException ignored) {
            // NOP
        }

        statement.execute("create table industry(" +
                "industry_code char(2) not null," +
                "industry_name varchar(50)," +
                "primary key (industry_code))");


        statement.execute("insert into industry values('00','name0')");
        statement.execute("insert into industry values('01','name1')");
        statement.execute("insert into industry values('02','name2')");

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