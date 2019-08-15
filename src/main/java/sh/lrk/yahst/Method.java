package sh.lrk.yahst;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public enum Method {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    TRACE("TRACE"),
    PATCH("PATCH");

    private final String httpRepresentation;

    Method(String httpRepresentation) {
        this.httpRepresentation = httpRepresentation;
    }

    public static Method parse(String method) {
        AtomicReference<Method> res = new AtomicReference<>();
        Arrays.asList(values()).forEach(v -> {
            if (v.httpRepresentation.equals(method.trim().toUpperCase())) {
                res.set(v);
            }
        });
        return res.get();
    }

    public String getHttpRepresentation() {
        return httpRepresentation;
    }
}
