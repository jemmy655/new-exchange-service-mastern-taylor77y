package cn.xa87.common.utils.http;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * http请求工具（调用第三方接口）
 */
public final class HttpUtil {

    private final static int REQ_EX_CODE = -99;
    private final static int TIME_OUT = 10 * 10000000; // 超时时间

    private HttpUtil() {
    }


    public static RespData reqConnection(ReqData reqData) {
        URL url = null;
        BufferedWriter out = null;
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        RespData respData = null;
        try {
            //设置请求参数
            StringBuilder arg = null;
            String para = "";
            Map<String, Object> parameter = reqData.getParameter();
            if (null != parameter && !parameter.isEmpty()) {
                arg = new StringBuilder();
                for (Map.Entry<String, Object> entry : parameter.entrySet()) {
                    arg.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                para = "?" + arg.substring(0, arg.length() - 1);
            }
            url = new URL(reqData.getUrl() + para);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (0 != reqData.getTimeOut()) {
                urlConnection.setReadTimeout(reqData.getTimeOut());
                urlConnection.setConnectTimeout(reqData.getTimeOut());
            }
            urlConnection.setRequestMethod(reqData.getMethod().toUpperCase());
            urlConnection.setRequestProperty("Content-Type", reqData.getContentType());
            //设置请求头
            Map<String, String> hMap = reqData.getHeader();
            if (null != hMap && !hMap.isEmpty()) {
                for (Map.Entry<String, String> entry : hMap.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            urlConnection.connect();
            if (ReqType.POST.get().equals(reqData.getMethod()) || ReqType.PUT.get().equals(reqData.getMethod())) {
                out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), Charset.forName("utf-8")));
            }
            //设置请求内容(json字符串的请求)
            String content = reqData.getReqContent();
            if (!isEmpty(content)) {
                if (null == out) {
                    out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), Charset.forName("utf-8")));
                }
                out.write(content);
                out.flush();
            }
            respData = getRespData(reader, urlConnection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            respData = createRespData(REQ_EX_CODE, "URL不正确。");
        } catch (ProtocolException e) {
            e.printStackTrace();
            respData = createRespData(REQ_EX_CODE, "协议不正确。");
        } catch (IOException e) {
            e.printStackTrace();
            respData = createRespData(REQ_EX_CODE, "数据流读写失败。");
        } finally {
            close(out);
            close(reader);
            if (null != urlConnection) {
                urlConnection.disconnect();
                urlConnection = null;
            }
        }
        return respData;
    }

    private static RespData getRespData(BufferedReader reader, HttpURLConnection urlConnection) throws IOException {
        RespData respData = null;
        int code = urlConnection.getResponseCode();
        switch (code) {
            case 200:
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("utf-8")));
                String data = null;
                StringBuilder result = new StringBuilder();
                while (null != (data = reader.readLine())) {
                    result.append(data);
                }
                String resultStr = result.toString();
                try {
                    respData = createRespData(JSON.parse(resultStr));
                } catch (Exception e) {
                    //返回string:可能是html,xml...
                    respData = createRespData(200, RespType.STRING, resultStr);
                }
                result.delete(0, result.length());
                result = null;
                break;
            case 302:
                respData = createRespData(302, "编码302：请求定向失败。");
                break;
            case 400:
                respData = createRespData(400, "编码400：请求出现语法错误。");
                break;
            case 403:
                respData = createRespData(403, "编码403：资源不可用。服务器理解客户的请求，但拒绝处理它。通常由于服务器上文件或目录的权限设置导致。");
                break;
            case 404:
                respData = createRespData(404, "编码404：无法找到指定位置的资源。");
                break;
            case 405:
                respData = createRespData(405, "编码405：请求方法（GET、POST、HEAD、DELETE、PUT、TRACE等）对指定的资源不适用。");
                break;
            case 500:
                respData = createRespData(500, "编码500：服务器遇到了意料不到的情况，不能完成客户的请求。");
                break;
            default:
                respData = createRespData(code, "请求发生错误，错误编码：" + code);
                break;
        }
        return respData;
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public static ReqData createReq() {
        return createReq(null, null);
    }

    /**
     * 获取请求参数
     *
     * @param header 请求头
     * @return
     */
    public static ReqData createReq(Map<String, String> header) {
        return createReq(header, null);
    }

    /**
     * 获取请求参数
     *
     * @param header    请求头
     * @param parameter 请求参数
     * @return
     */
    public static ReqData createReq(Map<String, String> header, Map<String, Object> parameter) {
        return ReqData.create(header, parameter);
    }

    /**
     * @param data 返回的数据结果
     * @return
     */
    private static RespData createRespData(Object data) {
        return createRespData(200, data);
    }

    /**
     * @param code Http返回的状态
     * @param data 请求返回的数据结果
     * @return
     */
    private static RespData createRespData(int code, Object data) {
        return RespData.create(code, data);
    }

    private static RespData createRespData(int code, RespType respType, Object data) {
        return RespData.create(code, respType, data);
    }

    private static boolean isEmpty(String str) {
        return null == str || "".equals(str.trim()) || "null".equals(str.trim());
    }

    private static void close(Closeable stream) {
        try {
            if (null != stream) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stream = null;
    }
}
