package cn.xa87.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间计算器
 * @author Administrator
 */
public class DataUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static String calculateDate(String dateTimeStr, String pattern, int amount, boolean add) {
        // 判断是否为日期或日期时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (pattern.equals(DATE_FORMAT)) {
            LocalDate date = LocalDate.parse(dateTimeStr, formatter);
            LocalDate newDate = add ? date.plusDays(amount) : date.minusDays(amount);
            return newDate.format(formatter);
        } else if (pattern.equals(DATETIME_FORMAT)) {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            LocalDateTime newDateTime = add ? dateTime.plusDays(amount) : dateTime.minusDays(amount);
            return newDateTime.format(formatter);
        } else {
            throw new IllegalArgumentException("Unsupported pattern: " + pattern);
        }
    }

    private static Date stringToDate(String dateString, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(dateString);
    }

    public static boolean isDate(Date date1,Date date2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }


    /**
     * 指定天数后的时间
     * @param dateTimeStr 表示要处理的日期或日期时间字符串
     * @param pattern 表示该字符串的格式
     * @param days  表示要添加天数
     * @return
     */
    public static Date addDays(String dateTimeStr, String pattern, int days) {
        try {
            return stringToDate(calculateDate(dateTimeStr, pattern, days, true),pattern);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 指定天数前的时间
     * @param dateTimeStr 表示要处理的日期或日期时间字符串
     * @param pattern 表示该字符串的格式
     * @param days  表示要减去的天数
     * @return
     */
    public static Date subtractDays(String dateTimeStr, String pattern, int days) {
        try {
            return stringToDate(calculateDate(dateTimeStr, pattern, days, false),pattern);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
