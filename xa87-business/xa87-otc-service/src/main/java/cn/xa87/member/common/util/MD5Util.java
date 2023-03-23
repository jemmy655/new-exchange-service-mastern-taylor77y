package cn.xa87.member.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author peter
 * @create 2019-03-29-18:08
 **/
public class MD5Util {

    public static String md5(String buffer) throws UnsupportedEncodingException {
        String string       = null;
        char hexDigist[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(buffer.getBytes("UTF-8"));
            byte[] datas = md.digest(); //16个字节的长整数

            char[] str = new char[2*16];
            int k = 0;

            for(int i=0;i<16;i++)
            {
                byte b   = datas[i];
                str[k++] = hexDigist[b>>>4 & 0xf];//高4位
                str[k++] = hexDigist[b & 0xf];//低4位
            }
            string = new String(str);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return string;
    }
}
