package io.github.util;

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
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     */
    public static String toUpperCaseFirstOne(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}
