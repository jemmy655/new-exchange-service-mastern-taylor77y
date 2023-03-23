package cn.xa87.data.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.constants.SYSconfig;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.HttpUtil;
import cn.xa87.common.utils.SmsSample;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.SmsConstant;
import cn.xa87.data.mapper.MemberMapper;
import cn.xa87.data.mapper.SysConfigMapper;
import cn.xa87.data.service.SmsService;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {


    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private Xa87RedisRepository redisRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public boolean sendSms(String phone, String phoneCode, SmsConstant.Sms_Type type) {

        final String access_key_id = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_ID);
        final String access_key_secret = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_SECRET);
        final String regionid = sysConfigMapper.selectByParamKey(SYSconfig.REGIONID);
        final String RegisterSignName = sysConfigMapper.selectByParamKey(SYSconfig.RegisterSignName);
        final String RegisterTemplateCode = sysConfigMapper.selectByParamKey(SYSconfig.RegisterTemplateCode);
        final String exchangeName = sysConfigMapper.selectByParamKey(SYSconfig.EXCHANGE_NAME);
        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;
        DefaultProfile profile = DefaultProfile.getProfile(regionid, access_key_id, access_key_secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionid);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", RegisterSignName);
        request.putQueryParameter("TemplateCode", RegisterTemplateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");
        try {
            //阿里云Sms服务
            /*CommonResponse response = client.getCommonResponse(request);
            String s = response.getData();
            JSONObject parse = (JSONObject) JSONObject.parse(s);
            log.info(parse.toJSONString());*/
            //v2sms服务
            String str="【" + exchangeName + "】Dear users, your dynamic verification code is: "+code+", valid within 5 minutes, please do not disclose to others!";
            if(null==phoneCode || phoneCode.isEmpty() || phoneCode.equals("86")){
                str = "【" + exchangeName + "】親愛的用戶，您的動態驗證碼是："+code+"，5分鐘內有效，請不要洩露給他人！";
            }
            final boolean flag = HttpUtil.sendSms(phone, str,phoneCode);
            if (flag) {
                redisRepository.setExpire(CacheConstants.DEFAULT_CODE_KEY + phone + "-" + type, String.valueOf(code), 300);
                Map<String, Object> map = new HashMap<>();
                map.put("account", phone);
                map.put("msg", str);
                map.put("type", type);
                memberMapper.insertRecord(map);
            }
            //阿里云Sms服务
            /*if (parse.get("Message").toString().equals("OK")) {
                redisRepository.setExpire(CacheConstants.DEFAULT_CODE_KEY + phone + "-" + type, String.valueOf(code), 300);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    public boolean sendSmsNew(String phone, SmsConstant.Sms_Type type){
        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;

        final String exchangeName = sysConfigMapper.selectByParamKey(SYSconfig.EXCHANGE_NAME);

        String username = "17620616315"; //在短信宝注册的用户名
        String password = "e4fca685918346fba859aeb5d3e1d536"; //在短信宝注册的密码
        String content="【" + exchangeName + "】Dear users, your dynamic verification code is: "+code+", valid within 5 minutes, please do not disclose to others!";
        String httpUrl = "https://api.smsbao.com/wsms";

        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(username).append("&");
        httpArg.append("p=").append(md5(password)).append("&");
        httpArg.append("m=").append(encodeUrlString(phone, "UTF-8")).append("&");
        httpArg.append("c=").append(encodeUrlString(content, "UTF-8"));

        String result = request(httpUrl, httpArg.toString());

        if (result.equals("0")) {
            redisRepository.setExpire(CacheConstants.DEFAULT_CODE_KEY + phone + "-" + type, String.valueOf(code), 300);
            Map<String, Object> map = new HashMap<>();
            map.put("account", phone);
            map.put("msg", content);
            map.put("type", type);
            memberMapper.insertRecord(map);
        }

        System.out.println(result);

        return Boolean.TRUE;
    }


    public static void main(String[] args) {
        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;

//        final String exchangeName = sysConfigMapper.selectByParamKey(SYSconfig.EXCHANGE_NAME);

        String username = "17620616315"; //在短信宝注册的用户名
        String password = "e4fca685918346fba859aeb5d3e1d536"; //在短信宝注册的密码
        String content="【YCoin】Dear users, your dynamic verification code is: "+code+"";
        String httpUrl = "http://api.smsbao.com/wsms";

        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(username).append("&");
        httpArg.append("p=").append(password).append("&");
        httpArg.append("m=").append(java.net.URLEncoder.encode("+8618613968463")).append("&");
        httpArg.append("c=").append(encodeUrlString(content, "UTF-8"));
        System.out.println(httpArg.toString());
        String result = request(httpUrl, httpArg.toString());
        System.out.println(result);
    }


    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        System.out.println(httpUrl);
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





    @Override
    public boolean sendMail(String mailbox, SmsConstant.Sms_Type type) {

        final String access_key_id = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_ID);
        final String access_key_secret = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_SECRET);
        final String regionid = sysConfigMapper.selectByParamKey(SYSconfig.REGIONID);
        final String exchangeName = sysConfigMapper.selectByParamKey(SYSconfig.EXCHANGE_NAME);
        final String send_mail = sysConfigMapper.selectByParamKey(SYSconfig.SEND_MAIL);
        final String subject = sysConfigMapper.selectByParamKey(SYSconfig.SUBJECT);


        DefaultProfile profile = DefaultProfile.getProfile(regionid, access_key_id, access_key_secret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setRegionId(regionid);
        request.setAccountName(send_mail);
        request.setFromAlias(exchangeName);// 发信人昵称
        request.setAddressType(1);
        request.setReplyToAddress(false);
        request.setToAddress(mailbox);
        request.setSubject(subject);
        Random rand = new Random();
        int code = rand.nextInt(100000) + 100000;
        String msg = "【" + exchangeName + "】Your email verification code for this operation is:" + code + "Valid for 5 minutes, please ignore if not operated by yourself！";
        request.setHtmlBody(msg);
        try {
            SingleSendMailResponse response = client.getAcsResponse(request);
            if (null != response) {
                redisRepository.setExpire(CacheConstants.DEFAULT_CODE_KEY + mailbox + "-" + type, String.valueOf(code),
                        300);
                Map<String, Object> map = new HashMap<>();
                map.put("account", mailbox);
                map.put("msg", msg);
                map.put("type", type);
                memberMapper.insertRecord(map);
            } else {
                throw new BusinessException(AjaxResultEnum.EMAIL_VERIFICATION_CODE_FAILED_TO_SEND.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(AjaxResultEnum.EMAIL_VERIFICATION_CODE_FAILED_TO_SEND.getMessage());
        }
        return Boolean.TRUE;
    }

}
