package sh.lrk.yahst;

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

    public void add(Method method, String url, IResponse resp) {
        routes.put(method.getHttpRepresentation() + "_" + url, resp);
    }

    public void add(Method method, String url, String filepath) {
        routes.put(method.getHttpRepresentation() + "_" + url,
                req -> Response.fromFile(req, filepath));
    }

}