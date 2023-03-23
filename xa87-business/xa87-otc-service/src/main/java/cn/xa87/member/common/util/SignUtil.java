package cn.xa87.member.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author peter
 * @create 2020-04-10-14:48
 **/
public class SignUtil {

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params
     * 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 生成签名
     * @param obj
     * @return
     */
    public static String createSign(Object obj) {
        if(null == obj){
            return null;
        }
        if("".equals((obj+"").replace(" ",""))){
            throw new NullPointerException();
        }
        if(obj instanceof String || obj instanceof Integer || obj instanceof Byte ||
                obj instanceof Long || obj instanceof Double || obj instanceof Float ||
                obj instanceof Character || obj instanceof Short || obj instanceof Boolean){
            throw new ClassCastException();
        }
        Map<String, String> map = null;
        if (obj instanceof Map) {
            map = (Map) obj;
        } else {
            map = ParamUtil.objectToMap(obj);
        }
        map.remove("sign");
        String link = SignUtil.createLinkString(map);
        //System.out.println("排序后"+link);
        String md5 = null;
        try {
            md5 = MD5Util.md5(link);
        } catch (UnsupportedEncodingException e) {
            System.out.println("加密失败！");
            return null;
        }
        //System.out.println("签名:"+md5);
        return md5;
    }


}
