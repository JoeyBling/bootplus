package io.github.test;

import io.github.util.DateUtils;

import java.util.Calendar;

/**
 * Main Test
 *
 * @author Created by 思伟 on 2020/1/9
 */
public class New_Main {

    public static void main(String[] args) throws Exception {
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.setTime(DateUtils.parseDateTimeStr("2020-06-30"));
        lastMonth.add(Calendar.MONTH, -1);
        System.out.println(DateUtils.format(lastMonth.getTime()));

        Calendar now = Calendar.getInstance();
        now.setTime(lastMonth.getTime());
        now.add(Calendar.MONTH, 1);
        System.out.println(DateUtils.format(now.getTime()));

    }


}
