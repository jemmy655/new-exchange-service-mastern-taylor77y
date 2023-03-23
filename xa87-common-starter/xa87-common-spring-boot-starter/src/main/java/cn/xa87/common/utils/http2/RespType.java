package cn.xa87.common.utils.http2;

public enum RespType {
    JSON, STRING;

    private String type;

    private RespType() {
    }

    private RespType(String type) {
        this.type = type;
    }

    public String get() {
        return this.type;
    }
}