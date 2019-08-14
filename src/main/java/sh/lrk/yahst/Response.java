package sh.lrk.yahst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Class that represents a response.
 * <p>
 * Response code constants are according to
 * https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html.
 * <p>Please note: Using a byte array as body allows for a maximum
 * response size of ~2.147GB or 2,147MB as it's limited to {@link Integer#MAX_VALUE}.</p>
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public final class Response {

    public enum Status {
        OK("200 OK"),
        TEMPORARY_REDIRECT("307 Temporary Redirect"),
        BAD_REQUEST("400 Bad Request"),
        UNAUTHORIZED("401 Unauthorized"),
        NOT_FOUND("404 Not Found"),
        METHOD_NOT_ALLOWED("405 Method Not Allowed"),
        INTERNAL_SERVER_ERROR("500 Internal Server Error");

        private final String httpRepresentation;

        Status(String httpRepresentation) {
            this.httpRepresentation = httpRepresentation;
        }

        public String getHttpRepresentation() {
            return httpRepresentation;
        }
    }

    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_CSS = "text/css";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String IMAGE_XICON = "image/x-icon";
    public static final String IMAGE_JPEG = "image/jpeg";

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final byte[] body;
    private String header;

    Response(byte[] body) {
        this(body, Status.OK);
    }

    Response(byte[] body, Status status) {
        this(body, status, TEXT_PLAIN, false, null);
    }

    Response(byte[] body, Status status, String contentType) {
        this(body, status, contentType, false);
    }

    Response(byte[] body, Status status, String contentType, boolean addAllow) {
        this(body, status, contentType, addAllow, null);
    }

    Response(byte[] body, Status status, String contentType, boolean addAllow, String location) {
        this.body = body;
        buildHeader(status, contentType, addAllow, location);
    }

    public Response(String body) {
        this(body, Status.OK);
    }

    public Response(String body, Status status) {
        this(body, status, TEXT_PLAIN);
    }

    public Response(String body, Status status, String contentType) {
        this(body.getBytes(), status, contentType);
    }

    private void buildHeader(Status status, String contentType, boolean addAllow, String location) {
        Date date = new Date();
        String start = "HTTP/1.1 " + status.getHttpRepresentation() + "\r\n";
        header = "Date: " + date.toString() + "\r\n";
        header += "Content-Type: " + contentType + "\r\n";
        header += "Content-length: " + body.length + "\r\n";

        if (addAllow) {
            header += "Allow: GET, POST\r\n";
        }

        if (location != null) {
            header += "Location: " + location + "\r\n";
        }

        header += "\r\n";
        header = start + header;
    }

    static Response getGenericErrorResponse(Request req) {
        return new Response("Nothing found for url '" + req.getUrl() + "' with method '" + req.getMethod() + "'!",
                Status.NOT_FOUND, TEXT_PLAIN);
    }

    static Response fromFile(Request req, String path) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try {
                ClassLoader classLoader = Response.class.getClassLoader();
                InputStream resourceAsStream = classLoader.getResourceAsStream(path);
                if (resourceAsStream != null) {
                    int b;
                    while ((b = resourceAsStream.read()) != -1) {
                        os.write(b);
                    }
                } else {
                    throw new FileNotFoundException("Resource is null: '" + path + "'!");
                }
            } catch (FileNotFoundException e) {
                log.error("Unable to find file: '" + path + "'!", e);
                return Response.getGenericErrorResponse(req);
            } catch (IOException e) {
                log.error("Unable to read file: '" + path + "'!", e);
                return Response.getGenericErrorResponse(req);
            }

            if (path.endsWith(".css")) {
                return new Response(os.toByteArray(), Status.OK, TEXT_CSS);
            } else if (path.endsWith(".htm") || path.endsWith(".html")) {
                return new Response(os.toByteArray(), Status.OK, TEXT_HTML);
            } else if (path.endsWith(".ico")) {
                return new Response(os.toByteArray(), Status.OK, IMAGE_XICON);
            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                return new Response(os.toByteArray(), Status.OK, IMAGE_JPEG);
            } else {
                return new Response(os.toByteArray());
            }
        } catch (IOException e) {
            log.error("Unable to write response to: '" + req.getUrl() + "'!", e);
            return Response.getGenericErrorResponse(req);
        }
    }

    public byte[] getResponseBytes() {
        byte[] headerBytes = header.getBytes();
        byte[] res = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, res, 0, headerBytes.length);
        System.arraycopy(body, 0, res, headerBytes.length, body.length);
        return res;
    }
}