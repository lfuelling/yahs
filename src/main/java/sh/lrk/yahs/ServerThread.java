package sh.lrk.yahs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class that represents the thread the server is running on.
 * This is to be used internally only.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
class ServerThread implements Runnable {

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(ServerThread.class);

    private boolean shouldStop = false;
    private final Routes routes;
    private final int port;
    private final int maxSize;

    /**
     * Constructor.
     *
     * @param routes  the routes to use
     * @param port    the port to use
     * @param maxSize the maximum allowed request size
     */
    ServerThread(Routes routes, int port, int maxSize) {
        this.routes = routes;
        this.port = port;
        this.maxSize = maxSize;
    }

    /**
     * Method containing the main server loop.
     */
    @Override
    public void run() {
        try {
            Server server = new Server(routes, port, maxSize);
            log.info("Server started on port " + port + "!");
            while (!shouldStop) {
                try {
                    Request req = server.accept();
                    server.sendResponse(req);
                } catch (IOException e) {
                    log.error("Unable to handle request!", e);
                    if (Status.isStatus(e.getMessage())) {
                        server.sendResponse(new Response("", Status.parse(e.getMessage()), ContentType.TEXT_PLAIN));
                    }
                }
            }
            server.close();
        } catch (IOException e) {
            log.error("Unable to run server!", e);
        }
    }

    /**
     * Stops the server on the next loop.
     */
    void stop() {
        shouldStop = true;
    }
}