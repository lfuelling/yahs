package sh.lrk.yahst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
                break; //TODO: maybe don't just cut stuff but it's enough for now.
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
            if (req.getMethod().equals("GET") || req.getMethod().equals("POST")) {
                IResponse mappedResponse = routes.get(req.getMethod() + "_" + req.getUrl());
                if (mappedResponse == null) {
                    return Response.getGenericErrorResponse(req);
                }
                return mappedResponse.getResponse(req);
            } else {
                String message = "Method '" + req.getMethod() + "' is not allowed!";
                return new Response(message.getBytes(),
                        Response.Status.METHOD_NOT_ALLOWED, Response.TEXT_PLAIN, true);
            }
        } else {
            return new Response("Method is null!\n\n" + req.toString(),
                    Response.Status.INTERNAL_SERVER_ERROR, Response.TEXT_PLAIN);
        }
    }

    void sendResponse(Request req) throws IOException {
        Response resp = getResponse(req);
        try (OutputStream out = client.getOutputStream()) {
            out.write(resp.getResponseBytes());
        }
    }

    /**
     * This method is used to start the server.
     * @param routes the routes
     * @param port the port to listen on
     * @param maxSize the max allowed request body size
     */
    public static void start(Routes routes, int port, int maxSize) {
        log.info("Starting Server...");

        ServerThread server = new ServerThread(routes, port, maxSize);
        new Thread(server, "ServerThread").start();
    }
}