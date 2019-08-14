package sh.lrk.yahst;

public enum Method {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), OPTIONS("OPTIONS");

    private final String httpRepresentation;

    Method(String httpRepresentation) {
        this.httpRepresentation = httpRepresentation;
    }

    public String getHttpRepresentation() {
        return httpRepresentation;
    }
}
