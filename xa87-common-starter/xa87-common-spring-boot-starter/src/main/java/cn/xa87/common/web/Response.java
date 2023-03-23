package cn.xa87.common.web;

import cn.xa87.common.exception.IError;
import lombok.Data;

import java.io.Serializable;

/**
 * 说明：统一 Rest 返回对象
 *
 * @author zhangwei
 * @date 2017年11月18日23:45:05
 */
@Data
public class Response implements Serializable {

    private static final long serialVersionUID = -5359531292427290394L;

    private String errorCode;
    private String errorMessage;
    private String extMessage;
    private Object result;
    private Response.Status status;
    private Object data;

    public Response() {
        this.status = Response.Status.SUCCEED;
    }

    public Response(IError error) {
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
        this.status = Response.Status.FAILED;
    }

    public static Response success() {
        return new Response();
    }


    public static Response success(Object result) {
        Response response = new Response();
        response.setResult(result);
        return response;
    }

    public static Response failure(String errorCode, String errorMessage) {
        Response response = new Response();
        response.errorCode = errorCode;
        response.errorMessage = errorMessage;
        response.status = Status.FAILED;
        return response;
    }

    public static Response failure(IError error) {
        Response response = new Response();
        response.errorCode = error.getErrorCode();
        response.errorMessage = error.getErrorMessage();
        response.status = Status.FAILED;
        return response;
    }

    public static Response failure(String message) {
        Response response = new Response();
        response.setErrorMessage(message);
        response.status = Status.FAILED;
        return response;
    }

    public static Response failure(String message, Object data) {
        Response response = new Response();
        response.setErrorMessage(message);
        response.setData(data);
        response.status = Status.FAILED;
        return response;
    }

    public static Response warring(Object result) {
        Response response = new Response();
        response.setResult(result);
        response.status = Status.WARRING;
        return response;
    }

    public enum Status {
        /**
         * 状态
         */
        SUCCEED,
        WARRING,
        FAILED;

        Status() {
        }
    }
}
