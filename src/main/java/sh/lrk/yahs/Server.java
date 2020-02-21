package sh.lrk.yahs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static sh.lrk.yahs.ContentType.TEXT_PLAIN;
import static sh.lrk.yahs.Status.INTERNAL_SERVER_ERROR;

/**
 * Class that represents the server.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public final class Server {

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private ServerSocket socket;
    private Socket client;
    private Routes routes;
    private final int maxSize;

    Server(Routes routes, int port, int maxSize) throws IOException {
        socket = new ServerSocket(port);
        this.routes = routes;
        this.maxSize = maxSize;
    }

    Request accept() throws IOException {
        client = socket.accept();
        InputStream is = client.getInputStream();
        StringBuilder raw = new StringBuilder();
        int counter = 0;
        int c;
        do {
            if (counter >= maxSize) {
                throw new IOException(Status.PAYLOAD_TOO_LARGE.getHttpRepresentation());
            }
            counter++;
            c = is.read();
            raw.append((char) c);
        } while (is.available() > 0);
        return new Request(raw.toString());
    }

    void close() throws IOException {
        socket.close();
    }

    private Response getResponse(Request req) {
        if (req.getMethod() != null) {
            IResponse mappedResponse = routes.get(req.getMethod() + "_" + req.getUrl());
            if (mappedResponse == null) {
                IResponse catchAll = routes.get("ALL");
                if (catchAll != null) {
                    return catchAll.getResponse(req);
                }
                return Response.getGenericErrorResponse(req);
            }
            return mappedResponse.getResponse(req);
        } else {
            return new Response("Method is null!\n\n" + req.toString(),
                    INTERNAL_SERVER_ERROR, TEXT_PLAIN);
        }
    }

    void sendResponse(Request req) throws IOException {
        Response res = getResponse(req);
        try (OutputStream out = client.getOutputStream()) {
            out.write(res.getResponseBytes());
        }
    }

    void sendResponse(Response res) {
        try (OutputStream out = client.getOutputStream()) {
            out.write(res.getResponseBytes());
        } catch (IOException e) {
            log.error("Unable to send response!", e);
        }
    }

    /**
     * This method is used to start the server.
     *
     * @param routes  the routes
     * @param port    the port to listen on
     * @param maxSize the max allowed request body size
     */
    public static void start(Routes routes, int port, int maxSize) {
        log.info("Starting Server...");

        ServerThread st = new ServerThread(routes, port, maxSize);
        new Thread(st, "ServerThread").start();
    }
}