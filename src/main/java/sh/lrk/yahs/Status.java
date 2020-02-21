package sh.lrk.yahs;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This enum lists all available HTTP statuses according to https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public enum Status {
    OK("200 OK"),
    CREATED("201 Created"),
    ACCEPTED("202 Accepted"),
    NON_AUTHORITATIVE_INFORMATION("203 Non-Authoritative Information"),
    NO_CONTENT("204 No Content"),
    RESET_CONTENT("205 Reset Content"),
    PARTIAL_CONTENT("206 Partial Content"),

    MULTIPLE_CHOICE("300 Multiple Choice"),
    MOVED_PERMANENTLY("301 Moved Permanently"),
    FOUND("302 Found"),
    SEE_OTHER("303 See Other"), // Enhydra lutris
    NOT_MODIFIED("304 Not Modified"),
    TEMPORARY_REDIRECT("307 Temporary Redirect"),
    PERMANENT_REDIRECT("308 Permanent Redirect"),

    BAD_REQUEST("400 Bad Request"),
    UNAUTHORIZED("401 Unauthorized"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found"),
    METHOD_NOT_ALLOWED("405 Method Not Allowed"),
    NOT_ACCEPTABLE("406 Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED("407 Proxy Authentication Required"),
    REQUEST_TIMEOUT("408 Request TImeout"),
    CONFLICT("409 Conflice"),
    GONE("410 Gone"),
    LENGTH_REQUIRED("411 Length Required"),
    PRECONDITION_FAILED("412 Precondition Failed"),
    PAYLOAD_TOO_LARGE("413 Payload Too Large"),
    URI_TOO_LONG("414 URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE("415 Unsupported Media Type"),
    REQUEST_RANGE_NOT_SATISFIABLE("416 Request Range Not Satisfiable"),
    EXPECTATION_FAILED("417 Expectation Failed"),
    IM_A_TEAPOT("418 I'm a teapot"),
    MISDIRECTED_REQUEST("421 Misdirected Request"),
    TOO_EARLY("425 Too Early"),
    UPGRADE_REQURED("426 Upgrade Required"),
    PRECONDITION_REQUIRED("428 Precondition Required"),
    TOO_MANY_REQUEST("429 Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE("431 Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS("451 Unavailable For Legal Reasons"),

    INTERNAL_SERVER_ERROR("500 Internal Server Error"),
    NOT_IMPLEMENTED("501 Not Implemented"),
    BAD_GATEWAY("502 Bad Gateway"),
    SERVICE_UNAVAILABLE("503 Service Unavailable"),
    GATEWAY_TIMEOUT("504 Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED("505 HTTP Version Not Supported"),
    NETWORK_AUTHENTICATION_REQUIRED("511 Network Authentication Required");

    private final String httpRepresentation;

    /**
     * Constructor.
     *
     * @param httpRepresentation the value this status has in http
     */
    Status(String httpRepresentation) {
        this.httpRepresentation = httpRepresentation;
    }

    /**
     * Getter for the HTTP representation.
     *
     * @return a string containing the HTTP representation
     */
    public String getHttpRepresentation() {
        return httpRepresentation;
    }

    /**
     * This method can be used to determine a enum value from a given string.
     *
     * @param status a string containing a HTTP status
     * @return the enum value corresponding to the string or null if nothing matched
     */
    public static Status parse(String status) {
        AtomicReference<Status> res = new AtomicReference<>();
        Arrays.asList(values()).forEach(v -> {
            if (v.httpRepresentation.toUpperCase().equals(status.trim().toUpperCase())) {
                res.set(v);
            }
        });
        return res.get();
    }

    /**
     * This method can be used to check if a given string is a HTTP status.
     *
     * @param s the string containing a HTTP status
     * @return true if the string is a valid HTTP status (case insensitive)
     */
    public static boolean isStatus(String s) {
        for (Status status : values()) {
            if (status.httpRepresentation.toUpperCase().equals(s.trim().toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
