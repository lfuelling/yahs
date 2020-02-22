package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    public static final Request DUMMY_REQUEST = new Request("GET / HTTP/1.1\r\n");

    @Test
    void testGetGenericErrorResponse() {
        assertTrue(new String(Response.getGenericErrorResponse(DUMMY_REQUEST).getResponseBytes())
                .startsWith("HTTP/1.1 " + Status.NOT_FOUND.getHttpRepresentation()));
    }

    @Test
    void testFromFile() {
        String responseString = new String(Response.fromFile(DUMMY_REQUEST, "ok.txt", Status.OK).getResponseBytes());
        assertTrue(responseString.endsWith("200 OK"));
        assertTrue(responseString.startsWith("HTTP/1.1 " + Status.OK.getHttpRepresentation()));
    }
}