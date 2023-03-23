package cn.xa87.common.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * HmacSha256加密.
 *
 * @author ZYQ
 * @date 2020/2/7 10:42
 */
public class HmacSha256Util {

    /**
     * 生成 HmacSha256 密钥
     *
     * @param key 密钥字符串
     * @return SecretKeySpec
     */
    public static SecretKeySpec createHmacSha256Key(String key) {
        return new SecretKeySpec(key.getBytes(), "HmacSHA256");
    }

    /**
     * hmac-sha256加密
     *
     * @param text 内容
     * @param sk   密钥
     * @return 密文
     * @throws Exception e
     */
    public static String hmacSha256(String text, SecretKeySpec sk) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(sk);
        byte[] rawHmac = mac.doFinal(text.getBytes());
        return byte2hex(rawHmac);
    }

    private static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式。
            stmp = (java.lang.Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

}
