package cn.xa87.common.web;


import java.io.Serializable;

/**
 * 请求返回对象
 */

public class Otc365RespData implements Serializable {
    /**
     * 请求返回的编码
     */
    private int code = 200;
    /**
     * 返回类型：json,string...
     */
    private Boolean success=true;
    private String msg="success";
    /**
     * 请求返回的数据结果
     */
    private Object data;

    Otc365RespData() {
    }


    public Otc365RespData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Otc365RespData(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static Otc365RespData success(Boolean result) {
        Otc365RespData response = new Otc365RespData();
        if (!result){
            response.setCode(1001);
            response.setMsg("failed");
        }
        response.setSuccess(result);
        return response;
    }
    public static Otc365RespData failure(Object result) {
        Otc365RespData response = new Otc365RespData(201,"failed");
        response.setData(result);
        response.setSuccess(false);
        return response;
    }
    @Override
    public String toString() {
        return "RespData{" +
                "code=" + code +
                ", type=" + msg +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}