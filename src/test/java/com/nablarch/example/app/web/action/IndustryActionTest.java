package com.nablarch.example.app.web.action;

import nablarch.fw.web.HttpResponse;
import nablarch.test.core.http.RestTestSupport;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

/**
 * {@link IndustryAction}のテストクラス
 */
public class IndustryActionTest extends RestTestSupport {
    @Test
    public void testFind() throws JSONException {
        HttpResponse response = sendRequest(get("/api/industries"));
        String message = "業種一覧取得";
        assertStatusCode(message, HttpResponse.Status.OK,response);
        assertEquals(message,readTextResource("industry-list.json")
                ,response.getBodyString(), JSONCompareMode.LENIENT);
    }
}