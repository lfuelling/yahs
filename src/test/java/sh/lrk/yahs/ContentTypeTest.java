package sh.lrk.yahs;

import org.junit.jupiter.api.Test;

import javax.swing.text.AbstractDocument;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ContentType} enum.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
class ContentTypeTest {

    public static final String VALID = "text/plain";
    public static final String INVALID1 = "test/plain";
    public static final String INVALID2 = "Bluergh";

    @Test
    void testParse() {
        assertEquals(ContentType.parse(VALID), ContentType.TEXT_PLAIN);
        assertNull(ContentType.parse(INVALID1));
        assertNull(ContentType.parse(INVALID2));
        for (ContentType c : ContentType.values()) {
            assertEquals(ContentType.parse(c.getHttpRepresentation()), c);
        }
    }
}