package cn.xa87.member.common.util;

/**
 * @Description:
 * @Author: peter
 * @Version: 1.0
 * @Date: 2020/4/2 19:23
 * @Modify
 */
public class HttpResult<T> {

    /**
     * code
     */
    private Integer code;

    /**
     * msg
     */
    private String msg;

    /**
     * data
     */
    private T data;

    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "HttpResult{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + ", success=" + success + '}';
    }
}
