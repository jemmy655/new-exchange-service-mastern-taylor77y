package cn.xa87.common.utils.http2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;


/**
 * http请求工具（调用第三方接口）.
 *
 * @author ZYQ
 * @date 2020/2/20 15:15
 */
@Slf4j
public final class HttpUtil {

    private final static int REQ_EX_CODE = -99;
    // 连接超时时间
    private final static int CONNECT_TIME_OUT = 100000;
    //读取超时时间
    private final static int READ_TIME_OUT = 100000;

    private HttpUtil() {
    }

    public static RespData reqConnection(ReqData reqData) {
        HttpURLConnection urlConnection = null;
        RespData respData = new RespData();
        respData.setType(RespType.JSON);
        try {
            URL url = new URL(reqData.getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            // 是否读取参数
            urlConnection.setDoInput(true);
            // 是否输入参数
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(READ_TIME_OUT);
            urlConnection.setConnectTimeout(CONNECT_TIME_OUT);
            urlConnection.setRequestMethod(reqData.getMethod().toUpperCase());
            urlConnection.setRequestProperty("Content-Type", reqData.getContentType());
            urlConnection.setRequestProperty("Accept", "application/json");
            //设置请求头
            Map<String, String> hMap = reqData.getHeader();
            if (null != hMap && !hMap.isEmpty()) {
                for (Map.Entry<String, String> entry : hMap.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            urlConnection.connect();
            String body = JSON.toJSONString(reqData.getParameter());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(String.valueOf(JSON.parseObject(body)));
            writer.close();
            int responseCode = urlConnection.getResponseCode();
            InputStream is;
            if (responseCode >= 400) {
                is = urlConnection.getErrorStream();
            } else {
                is = urlConnection.getInputStream();
            }
            //1,在读取的过程中,将读取的内容存储值缓存中,然后一次性的转换成字符串返回
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //2,读流操作,读到没有为止(循环)
            byte[] buffer = new byte[1024];
            //3,记录读取内容的临时变量
            int temp = -1;
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);
            }
            //将流转换为字符串。
            String result = bos.toString();
            JSONObject jsonObject = JSONObject.parseObject(result);
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<提币返回值.[{}]>>>>>>>>>>>>>>>>>>>>>>>", jsonObject);
            String code = jsonObject.getString("code");
            respData.setCode(Integer.parseInt(code));
            respData.setData(jsonObject.getString("data"));
            respData.setType(jsonObject.getString("message"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            respData.setData("URL不正确");
        } catch (ProtocolException e) {
            e.printStackTrace();
            respData.setData("协议不正确");
        } catch (IOException e) {
            e.printStackTrace();
            respData.setData("数据流读写失败");
        } finally {
            urlConnection.disconnect();
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
}
