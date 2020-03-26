package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {
    private static final Request dummyGetRequest = new Request("GET / HTTP/1.1\r\n");
    private static final String dummyJson = "{\"foo\":\"bar\"}";
    private static final String rawPostRequest = "POST / HTTP/1.1\r\n" +
            "Host: example.com\r\n" +
            "Connection: close\r\n" +
            "Accept: */*\r\n" +
            "User-Agent: Mozilla/4.0 (compatible)\r\n" +
            "Content-Type: application/json\r\n" +
            "Content-Length: 13\r\n" +
            "\r\n" +
            dummyJson;

    @Test
    void testGetRequest() {
        assertEquals("1.1", dummyGetRequest.getHttpVersion());
        assertEquals(Method.GET, dummyGetRequest.getMethod());
        assertEquals("/", dummyGetRequest.getUrl());
    }

    @Test
    void testJsonPostRequest() {
        Request r = new Request(rawPostRequest);
        assertEquals(Method.POST, r.getMethod());
        assertEquals("/", r.getUrl());
        assertEquals("1.1", r.getHttpVersion());

        String[] jsonSplit = r.toString().split("\n\\{");
        String json = "{" + jsonSplit[jsonSplit.length - 1];
        assertEquals(dummyJson, json);
    }
}
