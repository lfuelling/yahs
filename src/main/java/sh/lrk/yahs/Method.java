package sh.lrk.yahs;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Enum that holds all the HTTP methods.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
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

    /**
     * Constructor.
     *
     * @param httpRepresentation a string containing the HTTP value
     */
    Method(String httpRepresentation) {
        this.httpRepresentation = httpRepresentation;
    }

    /**
     * Parses a given string and returns a matching enum value.
     *
     * @param method the string containing a HTTP method
     * @return a matching enum value or null if nothing matched
     */
    public static Method parse(String method) {
        AtomicReference<Method> res = new AtomicReference<>();
        Arrays.asList(values()).forEach(v -> {
            if (v.httpRepresentation.equals(method.trim().toUpperCase())) {
                res.set(v);
            }
        });
        return res.get();
    }

    /**
     * Get the HTTP value of a method.
     *
     * @return the HTTP value
     */
    public String getHttpRepresentation() {
        return httpRepresentation;
    }
}
