package cn.xa87.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    /**
     * 按分钟划分时间戳 / 按小时划分时间戳
     *
     * @param timestamp 时间戳
     * @param minute    分钟
     * @return
     */
    public static Long minuteTimestamp(Long timestamp, Integer minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        int nowMinute = cal.get(Calendar.MINUTE);
        int num = nowMinute % minute;
        if (num != 0) {
            cal.set(Calendar.MINUTE, nowMinute - num);
        }
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当前小时时间戳 mm:ss -> 00:00
     *
     * @param timestamp 时间戳
     * @return
     */
    public static Long hourTimestamp(Long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当前天时间戳 HH:mm:ss -> 08:00:00
     *
     * @param timestamp 时间戳
     * @return
     */
    public static Long dayTimestamp(Long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 以星期为单位划分时间戳
     * 交易所需要每次取周一   /////////////////////////////////////////////////////////////////////////////////////////////
     *
     * @param timestamp
     * @param week
     * @return
     */
    public static Long weekTimestamp(Long timestamp, Integer week) {
        log.debug("以星期为单位划分时间戳. timestamp:[{}],week:[{}]", timestamp, week);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        int nowWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int nowDay = cal.get(Calendar.DAY_OF_YEAR);
        int nowYear = cal.get(Calendar.YEAR);
        int num = nowWeek - week;
        if (num >= 0) {
            cal.set(Calendar.DAY_OF_YEAR, nowDay - num);
        } else {
            int i = nowDay - 7;
            if (i < 0) {
                int lastYear = nowYear - 1;
                Long lastYearTimestamp = getYearLast(lastYear);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(lastYearTimestamp);
                int lastNowWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                int lastNowDay = calendar.get(Calendar.DAY_OF_YEAR);
                int lastNum = lastNowWeek - week;
                calendar.set(Calendar.DAY_OF_YEAR, lastNowDay - lastNum);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return cal.getTime().getTime();
            } else {
                int abs = Math.abs(num);
                cal.set(Calendar.DAY_OF_YEAR, i + abs);
            }
        }
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 上个月开始时间戳
     *
     * @param date
     * @return
     */
    public static long lastMonthBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 上个月结束时间戳
     *
     * @param date
     * @return
     */
    public static long lastMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 昨天开始时间戳
     *
     * @param date date
     * @return
     */
    public static long lastDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 昨天结束时间戳
     *
     * @param date date
     * @return
     */
    public static long lastDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    private static Long getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime().getTime();
    }

    /**
     * 获取当年的最后时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @return
     */
    private Long getYearEndTime(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTimeInMillis();
    }

}
