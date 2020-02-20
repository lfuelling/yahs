package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    public static final String OK = "200 OK";
    public static final String INVALID1 = "200 LEL";
    public static final String INVALID2 = "Bluergh";

    @Test
    void testParse() {
        assertEquals(Status.parse(OK), Status.OK);
        assertNull(Status.parse(INVALID1));
        assertNull(Status.parse(INVALID2));
        for (Status s : Status.values()) {
            assertEquals(Status.parse(s.getHttpRepresentation()), s);
        }
    }

    @Test
    void testIsStatus() {
        assertTrue(Status.isStatus(OK));
        assertFalse(Status.isStatus(INVALID1));
        assertFalse(Status.isStatus(INVALID2));
        for (Status s : Status.values()) {
            assertTrue(Status.isStatus(s.getHttpRepresentation()));
        }
    }
}