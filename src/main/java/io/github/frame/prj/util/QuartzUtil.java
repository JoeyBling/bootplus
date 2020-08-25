package io.github.frame.prj.util;

import io.github.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 定时任务工具类
 *
 * @author Created by 思伟 on 2020/8/24
 */
public class QuartzUtil {

    /**
     * 判断当前任务是否会被执行到
     *
     * @param cronExpression cron表达式
     * @return boolean
     */
    public static boolean canRun(String cronExpression) {
        try {
            String[] timeStrArr = cronExpression.split(" ");
            if ("?".equals(timeStrArr[5])) {
                // 月模式
                int year = Integer.valueOf(timeStrArr[6]),
                        month = Integer.valueOf(timeStrArr[4]),
                        day = Integer.valueOf(timeStrArr[3]),
                        hour = Integer.valueOf(timeStrArr[2]),
                        minute = Integer.valueOf(timeStrArr[1]),
                        second = Integer.valueOf(timeStrArr[0]);
                Date taskDate = DateUtils.toDate(year, month, day, hour, minute, second);
                // 只对未来的任务进行添加
                return DateUtils.getBetweenTime(new Date(), taskDate, Calendar.SECOND) > 0;
            } else {
                // 周模式
                return false;
            }
        } catch (Exception e) {
            // do it fix
            return true;
        }
    }

}
