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
 * <p>
 * Response code constants are according to
 * https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html.
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

    public Response(byte[] body) {
        this(body, Status.OK);
    }

    public Response(byte[] body, Status status) {
        this(body, status, TEXT_PLAIN, null);
    }

    public Response(byte[] body, ContentType contentType) {
        this(body, Status.OK, contentType);
    }

    public Response(byte[] body, Status status, ContentType contentType) {
        this(body, status, contentType, null);
    }

    public Response(byte[] body, Status status, ContentType contentType, HashMap<String, String> headers) {
        this.body = body;
        buildHeader(status, contentType, headers);
    }

    public Response(String body) {
        this(body, Status.OK);
    }

    public Response(String body, Status status) {
        this(body, status, TEXT_PLAIN);
    }

    public Response(String body, ContentType contentType) {
        this(body, Status.OK, contentType);
    }

    public Response(String body, Status status, ContentType contentType) {
        this(body.getBytes(), status, contentType);
    }

    private void buildHeader(Status status, ContentType contentType, HashMap<String, String> headers) {
        Date date = new Date();
        String start = "HTTP/1.1 " + status.getHttpRepresentation() + "\r\n";
        header = "Date: " + date.toString() + "\r\n";
        header += "Content-Type: " + contentType + "\r\n";
        header += "Content-length: " + body.length + "\r\n";

        if (headers != null) {
            headers.forEach((k, v) ->
                    header += k.replaceAll("\r", "").replaceAll("\n", "") + ": " +
                            v.replaceAll("\r", "").replaceAll("\n", ""));
        }

        header += "\r\n";
        header = start + header;
    }

    public static Response getGenericErrorResponse(Request req) {
        return new Response("Nothing found for url '" + req.getUrl() + "' with method '" + req.getMethod() + "'!",
                Status.NOT_FOUND, TEXT_PLAIN);
    }

    public static Response fromFile(Request req, String path) {
        return fromFile(req, path, Status.OK);
    }

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
            } else {
                return new Response(os.toByteArray(), status, APPLICATION_OCTET_STREAM);
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