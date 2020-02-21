package sh.lrk.yahs;

/**
 * Interface to implement when adding a route that needs logic.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public interface IResponse {

    /**
     * Method that returns a response.
     *
     * @param req the request
     * @return the response
     */
    Response getResponse(Request req);
}
