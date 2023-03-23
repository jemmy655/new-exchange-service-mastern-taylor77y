package cn.xa87.common.utils.http2;

public enum ReqType {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String type;

    private ReqType() {
    }

    private ReqType(String type) {
        this.type = type;
    }

    public String get() {
        return this.type;
    }
}
