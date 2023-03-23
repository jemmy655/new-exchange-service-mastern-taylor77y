package cn.xa87.common.utils.http2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求对象
 */
@Getter
@Setter
@ToString
public class ReqData {

    /**
     * 请求的url
     */
    private String url;
    /**
     * 请求的方式:默认为GET
     */
    private String method = ReqType.POST.get();
    /**
     * 数据请求类型
     */
    private String contentType;
    /**
     * 数据请求内容：json字符串
     */
    private String reqContent;
    /**
     * 超时时间设置(单位：毫秒)
     */
    private int timeOut;
    /**
     * 请求参数信息
     */
    private Map<String, Object> parameter;
    /**
     * 请求头信息
     */
    private Map<String, String> header;
    private ReqData() {
    }

    public static ReqData create() {
        return new ReqData();
    }

    public static ReqData create(Map<String, String> header) {
        ReqData reqData = create();
        if (null != header && !header.isEmpty()) {
            reqData.header = header;
        }
        return reqData;
    }

    public static ReqData create(Map<String, String> header, Map<String, Object> parameter) {
        ReqData reqData = create(header);
        if (null != parameter && !parameter.isEmpty()) {
            reqData.parameter = parameter;
        }
        return reqData;
    }

    public ReqData addReqProperty(String key, String value) {
        if (null == this.header) {
            this.header = new HashMap<String, String>();
        }
        this.header.put(key, value);
        return this;
    }

    public ReqData addReqParameter(String key, Object value) {
        if (null == this.parameter) {
            this.parameter = new HashMap<String, Object>();
        }
        this.parameter.put(key, value);
        return this;
    }
}

