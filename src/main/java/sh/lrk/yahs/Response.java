package sh.lrk.yahs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import static sh.lrk.yahs.ContentType.*;

/**
 * Class that represents a response.
 * <p>Please note: Using a byte array as body allows for a maximum
 * response size of ~2.147GB or 2,147MB as it's limited to {@link Integer#MAX_VALUE}.</p>
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public final class Response {

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final byte[] body;
    private String header;

    /**
     * Constructor.
     *
     * @param body the response body
     */
    public Response(byte[] body) {
        this(body, Status.OK);
    }

    /**
     * Constructor.
     *
     * @param body   the response body
     * @param status the response {@link Status}
     */
    public Response(byte[] body, Status status) {
        this(body, status, TEXT_PLAIN, null);
    }

    /**
     * Constructor.
     *
     * @param body        the response body
     * @param contentType the response {@link ContentType}
     */
    public Response(byte[] body, ContentType contentType) {
        this(body, Status.OK, contentType);
    }

    /**
     * Constructor.
     *
     * @param body        the response body
     * @param status      the response {@link Status}
     * @param contentType the response {@link ContentType}
     */
    public Response(byte[] body, Status status, ContentType contentType) {
        this(body, status, contentType, null);
    }

    /**
     * Constructor.
     *
     * @param body        the response body
     * @param status      the response {@link Status}
     * @param contentType the response {@link ContentType}
     * @param headers     the response headers
     */
    public Response(byte[] body, Status status, ContentType contentType, HashMap<String, String> headers) {
        this.body = body;
        buildHeaders(status, contentType, headers);
    }

    /**
     * Constructor.
     *
     * @param body the response body
     */
    public Response(String body) {
        this(body, Status.OK);
    }

    /**
     * Constructor.
     *
     * @param body   the response body
     * @param status the response {@link Status}
     */
    public Response(String body, Status status) {
        this(body, status, TEXT_PLAIN);
    }

    /**
     * Constructor.
     *
     * @param body        the response body
     * @param contentType the response {@link ContentType}
     */
    public Response(String body, ContentType contentType) {
        this(body, Status.OK, contentType);
    }

    /**
     * Constructor.
     *
     * @param body        the response body
     * @param status      the response {@link Status}
     * @param contentType the response {@link ContentType}
     */
    public Response(String body, Status status, ContentType contentType) {
        this(body.getBytes(), status, contentType);
    }

    /**
     * Builds the response headers.
     *
     * @param status      the response {@link Status}
     * @param contentType the response {@link ContentType}
     * @param headers     the response headers
     */
    private void buildHeaders(Status status, ContentType contentType, HashMap<String, String> headers) {
        Date date = new Date();
        String start = "HTTP/1.1 " + status.getHttpRepresentation() + "\r\n";
        header = "Date: " + date.toString() + "\r\n";
        header += "Content-Type: " + contentType.getHttpRepresentation() + "\r\n";
        header += "Content-length: " + body.length + "\r\n";

        if (headers != null) {
            headers.forEach((k, v) ->
                    header += k.replaceAll("\r", "").replaceAll("\n", "") + ": " +
                            v.replaceAll("\r", "").replaceAll("\n", ""));
        }

        header += "\r\n\r\n";
        header = start + header;
    }

    /**
     * Generates a generic 404 response reflecting the URL and method back in the error message.
     *
     * @param req the request to respond to
     * @return the error response
     */
    public static Response getGenericErrorResponse(Request req) {
        return new Response("Nothing found for url '" + req.getUrl() + "' with method '" + req.getMethod() + "'!",
                Status.NOT_FOUND, TEXT_PLAIN);
    }

    /**
     * Generates a 200 (OK) response with the contents of a file as the response body.
     *
     * @param req  the request to respond to
     * @param path the path to the file to return
     * @return the generated response
     */
    public static Response fromFile(Request req, String path) {
        return fromFile(req, path, Status.OK);
    }

    /**
     * Generates a response containing a given file's content as response body.
     *
     * @param req    the request to response to
     * @param path   the path to the file to return
     * @param status the {@link Status} to respond with
     * @return the generated response
     */
    public static Response fromFile(Request req, String path, Status status) {
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
                return new Response(os.toByteArray(), status, TEXT_CSS);
            } else if (path.endsWith(".htm") || path.endsWith(".html")) {
                return new Response(os.toByteArray(), status, TEXT_HTML);
            } else if (path.endsWith(".ico")) {
                return new Response(os.toByteArray(), status, IMAGE_XICON);
            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                return new Response(os.toByteArray(), status, IMAGE_JPEG);
            } else if (path.endsWith(".js")) {
                return new Response(os.toByteArray(), status, TEXT_JAVASCRIPT);
            } else {
                return new Response(os.toByteArray(), status, APPLICATION_OCTET_STREAM);
            }
        } catch (IOException e) {
            log.error("Unable to write response to: '" + req.getUrl() + "'!", e);
            return Response.getGenericErrorResponse(req);
        }
    }

    /**
     * Method to get the complete response as a byte[].
     *
     * @return the response bytes
     */
    public byte[] getResponseBytes() {
        byte[] headerBytes = header.getBytes();
        byte[] res = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, res, 0, headerBytes.length);
        System.arraycopy(body, 0, res, headerBytes.length, body.length);
        return res;
    }
}