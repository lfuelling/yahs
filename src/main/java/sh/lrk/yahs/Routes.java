package sh.lrk.yahs;

import java.util.HashMap;

/**
 * Class that holds all the routes.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class Routes {

    /**
     * The routes.
     */
    private final HashMap<String, IResponse> routes;

    /**
     * Constructor.
     */
    public Routes() {
        routes = new HashMap<>();
    }

    IResponse get(String key) {
        return routes.get(key);
    }

    public void add(Method method, String path, IResponse resp) {
        routes.put(method.getHttpRepresentation() + "_" + path, resp);
    }

    public void add(Method method, String path, String filepath) {
        routes.put(method.getHttpRepresentation() + "_" + path,
                req -> Response.fromFile(req, filepath));
    }

}