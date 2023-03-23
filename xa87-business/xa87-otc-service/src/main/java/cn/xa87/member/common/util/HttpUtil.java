package cn.xa87.member.common.util;

import com.alibaba.fastjson.JSONObject;
import cn.xa87.member.common.constant.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);


    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static HttpResultUtil doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpResultUtil doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Consts.HTTP.CONNECT_TIMEOUT).setSocketTimeout(Consts.HTTP.SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        // 设置请求头
        ParamUtil.packageHeader(headers, httpGet);

        // 执行请求并获得响应结果
        return getHttpClientResultUtil(httpClient, httpGet);
    }

    /**
     * Description: 获得响应结果(json转化返回值)
     *
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static HttpResultUtil getHttpClientResultUtil(CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpMethod);
            // 获取返回结果
            if (null != httpResponse && null != httpResponse.getStatusLine()) {
                String content = "";
                if (null != httpResponse.getEntity()) {
                    content = EntityUtils.toString(httpResponse.getEntity(), Consts.HTTP.ENCODING);
                }
                log.info("返回结果："+content);
                HttpResult httpResult = null;
                httpResult = JSONObject.parseObject(content, HttpResult.class);
                return new HttpResultUtil(httpResponse.getStatusLine().getStatusCode(), httpResult);
            }
            return new HttpResultUtil(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    /**
     * 发送 post 请求
     *
     * @param url     地址
     * @param headers 头信息
     * @param json    json 格式参数
     * @return String
     */
    public static HttpResultUtil doPostBody(String url, Map<String, String> headers, String json) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Consts.HTTP.CONNECT_TIMEOUT).setSocketTimeout(Consts.HTTP.SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);

        // 设置请求头
        ParamUtil.packageHeader(headers, httpPost);
        try {
            StringEntity stringEntity = new StringEntity(json,Consts.HTTP.ENCODING);
            //发送json数据需要设置contentType
            stringEntity.setContentType(Consts.HTTP.CONTENT_TYPE_JSON);
            httpPost.setEntity(stringEntity);
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        }
        return getHttpClientResultUtil(httpClient, httpPost);
    }
}
