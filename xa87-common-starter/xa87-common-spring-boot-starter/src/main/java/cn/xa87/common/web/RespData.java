package cn.xa87.common.web;


import java.io.Serializable;

/**
 * 请求返回对象
 */

public class RespData implements Serializable {
    /**
     * 请求返回的编码
     */
    private int code = -99;
    /**
     * 返回类型：json,string...
     */
    private Object type;
    /**
     * 请求返回的数据结果
     */
    private Object data;

    RespData() {
    }


    public RespData(int code, Object type) {
        this.code = code;
        this.type = type;
    }

    public RespData(int code, Object type, Object data) {
        this.code = code;
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "RespData{" +
                "code=" + code +
                ", type=" + type +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}