package cn.xa87.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class HttpUtil {

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setRequestProperty("Connection", "Keep-Alive");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(5000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(10000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            is = connection.getInputStream();
            // 封装输入流is，并指定字符集
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            // 存放数据
            StringBuffer sbf = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }  finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static HttpResponse doGet(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        return httpClient.execute(request);
    }

    public static String postSms(String url, String params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity sEntity = new StringEntity(params, "UTF-8");
            httpPost.setEntity(sEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return null;
    }

    public static String doPost(String url, Map<String, Object> headMap, Map<String, Object> paramMap) {
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        if (headMap != null) {
            Set<Entry<String, Object>> headSet = headMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Entry<String, Object>> headIterator = headSet.iterator();
            while (headIterator.hasNext()) {
                Entry<String, Object> mapEntry = headIterator.next();
                httpPost.addHeader(mapEntry.getKey(), mapEntry.getValue().toString());
            }
        }
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }



    public static boolean sendSms(String phone ,String content,String phoneCode){

        boolean flag = false;

        try {
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());

            System.out.println(dateString);

            String account = "1551303864915513038649"+dateString;

            final String encryption = encryption(account);

            String param = "action=send&userid=679&timestamp="+dateString+"&sign="+encryption+"&mobile="+phone+"&content="+content+"&sendTime=&extno=";
            ////v2sms服务
            String host = "http://106dxsms.com/v2sms.aspx?"+param;

            String s = doGet(host);
            if(StringUtils.contains(s,"ok")){
                flag = true;
            }*/
            if(null==phoneCode || phoneCode.isEmpty() || phoneCode.equals("86")){
                flag=SmsSample.sendSms(phone,content);
            }else{
                phone="+"+phoneCode+phone;
                flag=SmsSample.sendWSms(phone,content);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return  flag;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
/*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = sdf.format(new Date());

        System.out.println(dateString);

        String account = "1551303864915513038649"+dateString;

        final String encryption = encryption(account);

        System.out.println(encryption);

        String str = "【SOFTIO】您好，您的验证码是@，请切勿告诉他人。如非本人操作，请忽略本短信！";
        //str = new String(str.getBytes("gbk"),"utf-8");

        String param = "action=send&userid=679&timestamp="+dateString+"&sign="+encryption+"&mobile=13888888888&content="+str+"&sendTime=&extno=";
        String host = "http://106dxsms.com/v2sms.aspx?"+param;

        String s = doGet(host);
        System.out.println(s);*/
    }

    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
