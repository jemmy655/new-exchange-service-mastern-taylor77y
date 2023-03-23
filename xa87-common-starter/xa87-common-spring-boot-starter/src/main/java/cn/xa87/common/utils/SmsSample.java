package cn.xa87.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SmsSample {

    public static void main(String[] args) {
        String phone="18883168110";
        String code="3214";
        String exchangeName="MBTC";
        String str = "【" + exchangeName + "】尊敬的用户，您的动态验证码为："+code+"，5分钟内有效，请勿泄漏于他人！";

        SmsSample.sendSms(phone,str);
    }
    public static boolean sendWSms(String phone ,String content) {

        String testUsername = "MBTC"; //在短信宝注册的用户名
        String testPassword = "ccr666666"; //在短信宝注册的密码
        String testPhone = phone;
        String testContent = content;
        String httpUrl = "http://api.smsbao.com/wsms";

        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(md5(testPassword)).append("&");
        httpArg.append("m=").append(encodeUrlString(testPhone,"UTF-8")).append("&");
        httpArg.append("c=").append(encodeUrlString(testContent, "UTF-8"));
        String result = request(httpUrl, httpArg.toString());
        System.out.println(httpArg.toString());
        System.out.println(result);
        if(result.equals("0")){
            return true;
        }
        return false;
    }
    public static boolean sendSms(String phone ,String content) {

        String testUsername = "MBTC"; //在短信宝注册的用户名
        String testPassword = "ccr666666"; //在短信宝注册的密码
        String testPhone = phone;
        String testContent = content;
        String httpUrl = "http://api.smsbao.com/sms";

        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(md5(testPassword)).append("&");
        httpArg.append("m=").append(encodeUrlString(testPhone,"UTF-8")).append("&");
        httpArg.append("c=").append(encodeUrlString(testContent, "UTF-8"));
        String result = request(httpUrl, httpArg.toString());
        System.out.println(httpArg.toString());
        System.out.println(result);
        if(result.equals("0")){
            return true;
        }
        return false;
    }
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }
}

