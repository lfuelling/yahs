package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodTest {

    public static final String VALID = "GET";
    public static final String INVALID1 = "GIT";
    public static final String INVALID2 = "Bluergh";

    @Test
    void testParse() {
        assertEquals(Method.parse(VALID), Method.GET);
        assertNull(Method.parse(INVALID1));
        assertNull(Method.parse(INVALID2));
        for (Method m : Method.values()) {
            assertEquals(Method.parse(m.getHttpRepresentation()), m);
        }
    }
}