package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoutesTest {

    @Test
    void testAddGet() {
        Routes routes = new Routes();
        IResponse iResponse = req -> {
            if (req.getAttribute("a").equals("b")) {
                return new Response("", Status.OK, ContentType.TEXT_PLAIN);
            }
            return new Response("", Status.OK, ContentType.APPLICATION_OCTET_STREAM);
        };
        routes.add(Method.GET, "/", iResponse);
        assertEquals(routes.get(Method.GET.getHttpRepresentation() + "_/"), iResponse);
    }

    @Test
    void testAddGetFile() {
        Routes routes = new Routes();
        routes.add(Method.GET, "/", "ok.txt");
        assertTrue(new String(routes.get(Method.GET.getHttpRepresentation() + "_/")
                .getResponse(new Request("GET / HTTP/1.1\r\n")).getResponseBytes()).endsWith("200 OK"));
    }
}