package cn.xa87.member.common.util;

/**
 * 封装httpClient响应结果
 * @author peter
 * @create 2020-03-26-14:44
 **/
public class HttpResultUtil {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private HttpResult content;

    public HttpResultUtil(int code, HttpResult content) {
        this.code = code;
        this.content = content;
    }

    public HttpResultUtil() {
    }

    public HttpResultUtil(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HttpResult getContent() {
        return content;
    }

    public void setContent(HttpResult content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HttpClientResultUtil{" + "code=" + code + ", content=" + content + '}';
    }
}
