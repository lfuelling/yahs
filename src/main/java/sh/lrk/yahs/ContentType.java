package sh.lrk.yahs;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public enum ContentType {
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    TEXT_PLAIN("text/plain"),
    IMAGE_XICON("image/x-icon"),
    IMAGE_JPEG("image/jpeg"),
    APPLICATION_OCTET_STREAM("application/octet-stream");

    private final String httpRepresentation;

    ContentType(String httpRepresentation) {
        this.httpRepresentation = httpRepresentation;
    }

    public String getHttpRepresentation() {
        return httpRepresentation;
    }

    public static ContentType parse(String method) {
        AtomicReference<ContentType> res = new AtomicReference<>();
        Arrays.asList(values()).forEach(v -> {
            if (v.httpRepresentation.equals(method.trim().toUpperCase())) {
                res.set(v);
            }
        });
        return res.get();
    }
}
