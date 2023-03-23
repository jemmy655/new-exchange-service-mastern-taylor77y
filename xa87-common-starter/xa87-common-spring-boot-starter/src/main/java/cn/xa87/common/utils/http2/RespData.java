package cn.xa87.common.utils.http2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 请求返回对象
 */
@Getter
@Setter
@ToString
public class RespData {
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

    public static RespData create() {
        return create(200, null);
    }

    public static RespData create(Object data) {
        return create(200, data);
    }

    public static RespData create(int code, Object data) {
        RespData respData = new RespData();
        respData.setCode(code);
        respData.setData(data);
        respData.setType(RespType.JSON);
        return respData;
    }

    public static RespData create(int code, RespType respType, Object data) {
        RespData respData = new RespData();
        respData.setCode(code);
        respData.setData(data);
        respData.setType(respType);
        return respData;
    }
}