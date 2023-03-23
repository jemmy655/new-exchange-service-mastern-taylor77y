package cn.xa87.common.redis.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 限流script工具类
 */
@Slf4j
public class LimitScriptUtil {

    /**
     * return lua script String
     *
     * @param path 路劲
     * @return lua string
     */
    public static String getScript(String path) {
        StringBuilder sb = new StringBuilder();
        InputStream stream = LimitScriptUtil.class.getClassLoader().getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        try {
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str).append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("read lua script string failed.", e);
        }
        return sb.toString();
    }
}
