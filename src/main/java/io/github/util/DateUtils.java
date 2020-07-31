package io.github.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期处理
 *
 * @author Created by 思伟 on 2020/6/6
 */
public class DateUtils {
    /**
     * 日期格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式(HH:mm:ss)
     */
    public final static String TIME_PATTERN = "HH:mm:ss";

    /**
     * 日期时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时区
     */
    public static final String DATE_TIMEZONE = "GMT+8";

    /**
     * 安全用法
     */
    private static final ThreadLocal<DateFormat> TL_DATE_FORMATTER =
            ThreadLocal.withInitial(() -> {
                DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
                dateFormat.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));
                return dateFormat;
            });

    /**
     * 日期格式化所有格式
     */
    @Deprecated
    private final static SimpleDateFormat[] SIMPLE_DATE_FORMATS;

    static {
        // 静态初始化
        List<SimpleDateFormat> dateFormatList = Arrays.asList(
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy年MM月dd日"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new SimpleDateFormat("yy/MM/dd HH:mm:ss"),
                new SimpleDateFormat("yyyy/MM/dd"),
                new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"));
        SIMPLE_DATE_FORMATS = dateFormatList.toArray(new SimpleDateFormat[0]);
    }

    /**
     * 转换日期为String使用默认的格式
     *
     * @param date Date
     * @return String
     * @see #DATE_TIME_PATTERN
     */
    public static String format(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * 格式化日期
     */
    public static String format(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    /**
     * 转换日期为String
     *
     * @param date    Date
     * @param pattern 转换格式
     * @return String
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            if (null != pattern) {
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                dateFormat.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));
                TL_DATE_FORMATTER.set(dateFormat);
            }
            return TL_DATE_FORMATTER.get().format(date);
        } finally {
            TL_DATE_FORMATTER.remove();
        }
    }

    /**
     * 将字符串转为日期
     *
     * @see #parseDateStr(String, String)
     * @see #DATE_PATTERN
     */
    public static Date parseDateStr(String dateStr) throws ParseException {
        return parseDateStr(dateStr, DATE_PATTERN);
    }

    /**
     * 将字符串转为日期
     *
     * @see #parseDateStr(String, String)
     * @see #DATE_TIME_PATTERN
     */
    public static Date parseDateTimeStr(String dateStr) throws ParseException {
        return parseDateStr(dateStr, DATE_TIME_PATTERN);
    }

    /**
     * 将字符串转为日期
     *
     * @param dateStr 日期字符串
     * @param pattern 转换格式
     * @return Date
     * @throws ParseException
     */
    public static Date parseDateStr(String dateStr, String pattern) throws ParseException {
        if (dateStr == null) {
            return null;
        }
        try {
            if (null != pattern) {
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                dateFormat.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));
                TL_DATE_FORMATTER.set(dateFormat);
            }
            return TL_DATE_FORMATTER.get().parse(dateStr);
        } finally {
            TL_DATE_FORMATTER.remove();
        }
    }

    /**
     * 获取当前时间戳(秒级别)
     *
     * @return 当前时间戳
     * @see #currentTimeStamp()
     */
    public static long currentSecondTimeStamp() {
        return currentTimeStamp() / 1000;
    }

    /**
     * 获取当前时间戳(毫秒级别)
     *
     * @return 当前时间戳
     */
    public static long currentTimeStamp() {
        // 中国上海时区
        return LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 毫秒级别时间戳转换为Date
     *
     * @param timeStamp 时间戳(毫秒级别)
     * @return Date
     */
    public static Date getTimeStampDate(long timeStamp) {
        return new Date(timeStamp);
    }

    /**
     * 秒级别时间戳转换为Date
     *
     * @param timeStamp 时间戳(秒级别)
     * @return Date
     */
    public static Date getSecondTimeStampDate(long timeStamp) {
        return new Date(timeStamp * 1000);
    }

    /**
     * 判断日期是否是周末
     *
     * @param date 日期
     * @return boolean
     */
    public static boolean isWeekend(Date date) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(date);

        int i = myCalendar.get(Calendar.DAY_OF_WEEK);
        // 星期日i==1，星期六i==7
        return i == Calendar.SUNDAY || i == Calendar.SATURDAY;
    }

    /**
     * 判断日期是否是周末
     *
     * @param dateStr yyyy-MM-dd
     * @see #isWeekend(Date)
     */
    public static boolean isWeekend(String dateStr) throws ParseException {
        return isWeekend(parseDateStr(dateStr));
    }

    /**
     * 判断日期是否是周末
     *
     * @param secondTimeStamp 时间戳(秒级别)
     * @see #isWeekend(Date)
     */
    public static boolean isWeekendByTimestamp(long secondTimeStamp) throws Exception {
        return isWeekend(getSecondTimeStampDate(secondTimeStamp));
    }

    /**
     * 获得当天0点时间
     *
     * @return Date
     */
    public static Date getTimeMorning() {
        // 获得今天时间
        Calendar c = Calendar.getInstance();

        // 将时，分，秒，毫秒设置为0
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // 此处返回的为今天的零点的毫秒数
        return new Date(c.getTimeInMillis());
    }

}
