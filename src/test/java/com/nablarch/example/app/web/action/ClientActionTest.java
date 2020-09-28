package com.nablarch.example.app.web.action;

import nablarch.fw.web.HttpResponse;
import nablarch.test.core.http.RestTestSupport;
import org.junit.Test;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

/**
 * {@link ClientAction}のテストクラス
 */
public class ClientActionTest extends RestTestSupport {
    /**
     * 検索条件なしで顧客が検索できること
     */
    @Test
    public void testFind() {
        HttpResponse response = sendRequest(get("/api/clients"));
        assertStatusCode("顧客一覧取得", HttpResponse.Status.OK, response);
        with(response.getBodyString())
                .assertThat("$", hasSize(120));
    }

    /**
     * 業種コードを検索条件として顧客が検索できること
     */
    @Test
    public void testFindByIndustryCode() {
        HttpResponse response = sendRequest(get("/api/clients?industryCode=01"));
        assertStatusCode("業種コードで検索", HttpResponse.Status.OK,response);
        with(response.getBodyString())
                .assertThat("$",hasSize(1))
                .assertThat("$[0]",allOf(
                        hasEntry("clientName","１株式会社")
                        ,hasEntry("industryCode","01")
                        ,hasEntry("industryName","農業")));
    }

    /**
     * 顧客名を検索条件として顧客が検索できること
     */
    @Test
    public void testFindByClientName() {
        HttpResponse response = sendRequest(get("/api/clients?clientName=１１株式"));
        assertStatusCode("顧客名で検索", HttpResponse.Status.OK,response);
        with(response.getBodyString())
                .assertThat("$",hasSize(2))
                .assertThat("$..clientName",hasItems("１１株式会社","１１１株式会社"));
    }

    /**
     * 検索条件が不正な場合は400のレスポンスとメッセージが返却されること
     */
    @Test
    public void testFind_validationError() throws Exception {
        HttpResponse response = sendRequest(get("/api/clients?clientName=test"));
        assertStatusCode("顧客名で検索", HttpResponse.Status.BAD_REQUEST,response);
        with(response.getBodyString())
                .assertThat("$[0]",hasEntry("message","顧客名は64文字以下の全角文字で入力してください。"));
    }
}