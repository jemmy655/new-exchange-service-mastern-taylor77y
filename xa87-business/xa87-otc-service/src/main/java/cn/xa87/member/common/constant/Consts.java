package cn.xa87.member.common.constant;

/**
 * @Description: Http 常量
 * @Author: peter
 * @Version: 1.0
 * @Date: 2020/7/5 18:43
 * @Modify
 */
public interface Consts {

    interface HTTP{

        /* 响应成功 */
        Integer RESPONSE_SUCCESS = 200;

        /** 编码格式。发送编码格式统一用UTF-8 */
        String ENCODING = "UTF-8";

        //请求格式
        String CONTENT_TYPE = "Content-Type";

        // post json 请求格式
        String CONTENT_TYPE_JSON = "application/json";

        // get form 请求格式
        String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

        /** 设置连接超时时间，单位毫秒。 */
        int CONNECT_TIMEOUT = 6000;

        /** 请求获取数据的超时时间(即响应时间)，单位毫秒。 */
        int SOCKET_TIMEOUT = 6000;

        // http请求方式 post
        String HTTP_POST = "POST";

        // http请求方式 get
        String HTTP_GET = "GET";
    }

    interface COMMON{
        //测试链接
        static final String  URL = "https://open-v2.otc365.com/";

        //测试KEY
        static final String  APPKEY = "2020082998425690";

        //测试私钥
        static final String  APPSECRET = "e2b3ccfd90f8ff15a50e07dec9acc358";

    }
}
