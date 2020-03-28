package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Routes} class.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
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
        Request req = new Request("GET / HTTP/1.1\r\n", InetAddress.getLoopbackAddress());
        assertTrue(new String(routes.get(Method.GET.getHttpRepresentation() + "_/")
                .getResponse(req).getResponseBytes()).endsWith("200 OK"));
    }
}