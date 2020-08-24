package io.github.frame.prj.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 定时任务工具类
 *
 * @author Created by 思伟 on 2020/8/24
 */
public class TaskUtil {

    /**
     * 几秒之后的时间表达式
     *
     * @param before  时间
     * @param seconds 秒数
     * @return String
     */
    public static String getCronForSecondsAfter(Date before, int seconds) {
        Date after = addSeconds(before, seconds);
        return getCronForTime(after);
    }

    /**
     * 几分钟之后的时间表达式
     *
     * @param before  时间
     * @param minutes 分钟
     * @return String
     */
    public static String getCronForMinutesAfter(Date before, int minutes) {
        Date after = addMinutes(before, minutes);
        return getCronForTime(after);
    }

    /**
     * 几天后的时间表达式
     *
     * @param before 时间
     * @param days   天数
     * @return String
     */
    public static String getCronForDaysAfter(Date before, int days) {
        Date after = addDays(before, days);
        return getCronForTime(after);
    }

    /**
     * 根据时间获取时间表达式
     *
     * @param date 时间
     * @return 时间表达式
     */
    public static String getCronForTime(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        int day = getDay(date);
        int hour = getHour(date);
        int minute = getMinute(date);
        int second = getSecond(date);
        // ? 表示的是week
        return second + " " + minute + " " + hour + " " + day + " " + month + " ? " + year;
    }

    /**
     * 增加几秒
     *
     * @param before  增加前的时间
     * @param seconds 增加的秒数
     * @return 增加后的时间
     */
    private static Date addSeconds(Date before, int seconds) {
        if (before == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.SECOND, seconds);
        Date after = calendar.getTime();
        return after;
    }

    /**
     * 增加几分钟
     *
     * @param before  增加前的时间
     * @param minutes 分钟
     * @return 增加后的时间
     */
    private static Date addMinutes(Date before, int minutes) {
        if (before == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.MINUTE, minutes);
        Date after = calendar.getTime();
        return after;
    }

    /**
     * 增加几天
     *
     * @param before 增加前的时间
     * @param days   增加的天数
     * @return 增加后的时间
     */
    private static Date addDays(Date before, int days) {
        if (before == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date after = calendar.getTime();
        return after;
    }

    /**
     * 获得年份
     *
     * @param date 日期
     * @return 年
     */
    private static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得月
     *
     * @param date 日期
     * @return 月
     */
    private static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 得到月，因为从0开始的，所以要加1
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得天
     *
     * @param date 日期
     * @return 天
     */
    private static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 得到天
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得小时
     *
     * @param date 日期
     * @return 小时
     */
    private static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 得到小时
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得分钟
     *
     * @param date 日期
     * @return 分
     */
    private static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 得到分钟
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获得年份
     *
     * @param date 日期
     * @return 年
     */
    private static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 得到秒
        return calendar.get(Calendar.SECOND);
    }

    public static void main(String[] args) {
        final Date nowDate = new Date();
        System.out.println(getCronForSecondsAfter(nowDate, 5));
        System.out.println(QuartzUtil.canRun(getCronForSecondsAfter(nowDate, -5)));
        System.out.println(QuartzUtil.canRun(getCronForSecondsAfter(nowDate, +5)));
        System.out.println(getCronForMinutesAfter(nowDate, 5));
        System.out.println(getCronForDaysAfter(nowDate, 5));
    }

}
