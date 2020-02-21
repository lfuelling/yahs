package sh.lrk.yahs;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Enum that holds all known content types.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public enum ContentType {
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    TEXT_PLAIN("text/plain"),
    TEXT_JAVASCRIPT("text/javascript"),
    IMAGE_XICON("image/x-icon"),
    IMAGE_JPEG("image/jpeg"),
    APPLICATION_OCTET_STREAM("application/octet-stream");

    private final String httpRepresentation;

    /**
     * Constructor.
     *
     * @param httpRepresentation string containing the HTTP representation of the content type
     */
    ContentType(String httpRepresentation) {
        this.httpRepresentation = httpRepresentation;
    }

    /**
     * Get the HTTP representation of the content type.
     *
     * @return the HTTP representation
     */
    public String getHttpRepresentation() {
        return httpRepresentation;
    }

    /**
     * Method that tried to find a matching enum value for a given string containing a content type.
     *
     * @param contentType the string containing the content type
     * @return a matching enum value or null if nothing matched
     */
    public static ContentType parse(String contentType) {
        AtomicReference<ContentType> res = new AtomicReference<>();
        Arrays.asList(values()).forEach(v -> {
            if (v.httpRepresentation.toUpperCase().equals(contentType.trim().toUpperCase())) {
                res.set(v);
            }
        });
        return res.get();
    }
}
