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

    /**
     * Gets an {@link IResponse} for a route key.
     *
     * @param key the route key (consisting of the method, an underscore and the path; eg. <pre>GET_/foo</pre>)
     * @return the {@link IResponse} or null if nothing is found
     */
    IResponse get(String key) {
        return routes.get(key);
    }

    /**
     * Adds a route by using an {@link IResponse}.
     *
     * @param method the method to apply this route to
     * @param path   the path to apply this route to
     * @param res    the {@link IResponse} for this route
     */
    public void add(Method method, String path, IResponse res) {
        routes.put(method.getHttpRepresentation() + "_" + path, res);
    }

    /**
     * Adds a route by using a file (relative to the <pre>resources</pre> folder).
     * Note: the filename or path will not be reflected in the response!
     *
     * @param method   the method to apply this route to
     * @param path     the path to apply this route to
     * @param filepath the path to the file to be returned
     */
    public void add(Method method, String path, String filepath) {
        routes.put(method.getHttpRepresentation() + "_" + path,
                req -> Response.fromFile(req, filepath));
    }

    /**
     * Adds a catchall route that returns a file's content.
     *
     * @param filepath the path to the file
     */
    public void addCatchAll(String filepath) {
        routes.put("ALL", req -> Response.fromFile(req, filepath));
    }

    /**
     * Adds a catchall route by evaluating an {@link IResponse}.
     *
     * @param res the {@link IResponse} to evaluate
     */
    public void addCatchAll(IResponse res) {
        routes.put("ALL", res);
    }

}