package cn.xa87.member.common.util;

import cn.xa87.member.common.constant.Consts;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description: 对参数进行处理的方法存放的类
 * @Author: peter
 * @Version: 1.0
 * @Date: 2020/4/5 19:04
 * @Modify
 */
public class ParamUtil {

    private static Logger logger = LoggerFactory.getLogger(ParamUtil.class);

    /**
     * 将Object对象里面的属性和值(实体类)转化成Map对象
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object obj) {
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
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            map.put(fieldName, null == value?null:value+"");
        }
        return map;
    }

    /**
     * Description: 封装请求头
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (null != params) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }


}
