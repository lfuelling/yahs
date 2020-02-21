package sh.lrk.yahs;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that represents a request.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public final class Request {

    private String req;
    private Method method = null;
    private String url = null;
    private String httpVersion = null;
    private HashMap<String, String> attrs;

    /**
     * Constructor.
     *
     * @param req the request
     */
    Request(String req) {
        this.req = req;
        attrs = new HashMap<>();
        parse();
    }

    private void parse() {
        String[] temp = req.split("\r\n");
        String firstLine = temp[0];
        String[] firstLineSplit = firstLine.split(" ");
        if (firstLineSplit.length == 3) {
            method = Method.parse(firstLineSplit[0]);
            httpVersion = firstLineSplit[2];
            if (method.equals(Method.POST)) {
                url = firstLineSplit[1];
                setAttributes(temp[temp.length - 1]);
            } else if (method.equals(Method.GET)) {
                String[] arr = firstLineSplit[1].split("[?]");
                if (arr.length == 2) {
                    url = arr[0];
                    setAttributes(arr[1]);
                } else {
                    url = firstLineSplit[1];
                }
            } else {
                url = firstLineSplit[1];
            }
        }
    }

    private void setAttributes(String rawAttributes) {
        String[] attribs = rawAttributes.split("&");

        for (String attrib : attribs) {
            String[] attr = attrib.split("=");
            if (attr.length == 2) {
                setAttribute(attr[0], attr[1].replace("+", " "));
            }
        }
    }

    /**
     * Method to get a request attribute (eg. get param) by key.
     *
     * @param key the param key
     * @return the param value
     */
    public String getAttribute(String key) {
        String ret = attrs.get(key);
        if (ret == null) {
            return "null";
        }
        return ret;
    }

    private void setAttribute(String key, String value) {
        attrs.put(key, value);
    }

    /**
     * Get the request {@link Method}.
     *
     * @return the request method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Get the HTTP version used.
     *
     * @return the HTTP version
     */
    public String getHttpVersion() {
        return httpVersion;
    }

    /**
     * Get the request URL.
     *
     * @return the request url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Turn the request into a String.
     *
     * @return the request as string
     */
    public String toString() {
        return req;
    }
}