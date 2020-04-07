package io.github.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author Created by 思伟 on 2020/1/17
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 首字母转小写
     */
    public static String toLowerCaseFirstOne(String s) {
        if (isEmpty(s)) {
            return EMPTY;
        }
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     */
    public static String toUpperCaseFirstOne(String s) {
        if (isEmpty(s)) {
            return EMPTY;
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
        }
    }

    /**
     * 转换为Integer类型
     */
    public static Integer getIntegerValue(CharSequence cs) {
        return isNumeric(cs) ? Integer.getInteger(cs.toString()) : null;
    }

    /**
     * 校验手机号合法性
     */
    public static boolean checkMobile(String mobile) {
        return checkMobile(mobile, "1[0-9]{10}");
    }

    public static boolean checkMobile(String mobile, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mobile);

        return matcher.matches();
    }

    /**
     * 将对象转换为String
     */
    public static String toString(Object value) {
        String strValue;
        if (value == null) {
            strValue = null;
        } else if (value instanceof String) {
            strValue = (String) value;
        } else if (value instanceof BigDecimal) {
            strValue = ((BigDecimal) value).toString();
        } else if (value instanceof Integer) {
            strValue = ((Integer) value).toString();
        } else if (value instanceof Long) {
            strValue = ((Long) value).toString();
        } else if (value instanceof Float) {
            strValue = ((Float) value).toString();
        } else if (value instanceof Double) {
            strValue = ((Double) value).toString();
        } else if (value instanceof Boolean) {
            strValue = ((Boolean) value).toString();
        } else if (value instanceof Date) {
            strValue = DateUtils.format((Date) value, DateUtils.DATE_TIME_PATTERN);
        } else {
            strValue = value.toString();
        }
        return strValue;
    }

}
