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
    public static final String[] VALID_EXTENSIONS = {"txt", "css", "js", "html", "htm"};
    public static final String INVALID1_EXTENSION = "test/plain";
    public static final String INVALID2_EXTENSION = "Bluergh";

    @Test
    void testParse() {
        assertEquals(ContentType.parse(VALID), ContentType.TEXT_PLAIN);
        assertNull(ContentType.parse(INVALID1));
        assertNull(ContentType.parse(INVALID2));
        for (ContentType c : ContentType.values()) {
            assertEquals(ContentType.parse(c.getHttpRepresentation()), c);
        }
    }

    @Test
    void testGetByExtension() {
        assertEquals(ContentType.getByExtension(VALID_EXTENSIONS[0]), ContentType.TEXT_PLAIN);
        assertEquals(ContentType.getByExtension(VALID_EXTENSIONS[1]), ContentType.TEXT_CSS);
        assertEquals(ContentType.getByExtension(VALID_EXTENSIONS[2]), ContentType.TEXT_JAVASCRIPT);
        assertEquals(ContentType.getByExtension(VALID_EXTENSIONS[3]), ContentType.TEXT_HTML);
        assertEquals(ContentType.getByExtension(VALID_EXTENSIONS[4]), ContentType.TEXT_HTML);
        assertNull(ContentType.getByExtension(INVALID1_EXTENSION));
        assertNull(ContentType.getByExtension(INVALID2_EXTENSION));
        for (ContentType contentType : ContentType.values()) {
            if (contentType.getFileExtensions() != null) {
                for (String fileExtension : contentType.getFileExtensions()) {
                    assertEquals(ContentType.getByExtension(fileExtension), contentType);
                }
            }
        }
    }
}