package sh.lrk.yahs;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Enum that holds all known content types.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public enum ContentType {
    TEXT_HTML("text/html", "html", "htm"),
    TEXT_CSS("text/css", "css"),
    TEXT_PLAIN("text/plain", "txt"),
    TEXT_MARKDOWN("text/markdown", "md"),
    TEXT_JAVASCRIPT("text/javascript", "js"),
    IMAGE_XICON("image/x-icon", "ico"),
    IMAGE_JPEG("image/jpeg", "jpg", "jpeg"),
    APPLICATION_OCTET_STREAM("application/octet-stream");

    private final String httpRepresentation;
    private final String[] fileExtensions;

    /**
     * Constructor.
     *
     * @param httpRepresentation string containing the HTTP representation of the content type
     */
    ContentType(String httpRepresentation, String... fileExtensions) {
        this.httpRepresentation = httpRepresentation;
        this.fileExtensions = fileExtensions;
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
     * Get the file extensions of the content type.
     *
     * @return the file extensions
     */
    public String[] getFileExtensions() {
        return fileExtensions;
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

    /**
     * Method that tried to find a matching enum value for a given string containing an extension.
     *
     * @param extension the string containing the extension
     * @return a matching enum value or null if nothing matched
     */
    public static ContentType getByExtension(String extension) {
        AtomicReference<ContentType> res = new AtomicReference<>();
        Arrays.asList(values()).forEach(v -> {
            if (v.fileExtensions != null) {
                for (String ext : v.fileExtensions) {
                    if (ext.toUpperCase().equals(extension.trim().toUpperCase())) {
                        res.set(v);
                    }
                }
            }
        });
        return res.get();
    }
}
