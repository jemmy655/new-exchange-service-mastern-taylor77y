package cn.xa87.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 隐秘数据工具类
 */
public class HideDataUtil {

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param num
     * @return
     */
    public static String mobile(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 3)
            return email;
        else
            return StringUtils.rightPad(StringUtils.left(email, 3), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }
}
