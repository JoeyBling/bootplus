package io.github.util;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author Created by 思伟 on 2020/1/17
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 数字字符数组
     */
    private final static char[] NUM_CHAR_ARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 数字+大小写英文字符数组
     */
    private final static char[] STR_CHAR_ARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 未定义Str常量
     */
    public static final String UNDEFINED = "undefined";

    /**
     * @see #defaultIfUndefined(CharSequence, CharSequence)
     */
    public static String defaultIfUndefined(String str) {
        return defaultIfUndefined(str, EMPTY);
    }

    /**
     * 如果为空或undefined返回空
     *
     * @param str        T
     * @param defaultStr 默认值
     * @param <T>
     * @return CharSequence
     */
    public static <T extends CharSequence> T defaultIfUndefined(final T str, final T defaultStr) {
        if (isEmpty(str) || equalsIgnoreCase(str, UNDEFINED)) {
            return defaultStr;
        }
        return str;
    }

    /**
     * 如果是NULL返回默认值，否则返回本身
     *
     * @param str        T
     * @param defaultStr 默认值
     * @param <T>
     * @return CharSequence
     */
    public static <T extends CharSequence> T defaultIfNull(final T str, final T defaultStr) {
        return null == str ? defaultStr : str;
    }

    /**
     * 首字母转小写
     */
    public static String toLowerCaseFirstOne(String str) {
        if (isEmpty(str)) {
            return EMPTY;
        }
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0)))
                    .append(str.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     */
    public static String toUpperCaseFirstOne(String str) {
        if (isEmpty(str)) {
            return EMPTY;
        }
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0)))
                    .append(str.substring(1)).toString();
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
        return regexCheck(mobile, "1[0-9]{10}");
    }

    /**
     * 正则校验
     *
     * @param str   待校验字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean regexCheck(String str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
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
            strValue = DateUtils.format((Date) value);
        } else if (value.getClass().isArray()) {
            strValue = Arrays.toString((Object[]) value);
        } else if (value instanceof LocalDateTime) {
            strValue = DateUtils.format((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            strValue = DateUtils.format((LocalDate) value);
        } else if (value instanceof LocalTime) {
            strValue = DateUtils.format((LocalTime) value);
        } else {
            strValue = value.toString();
        }
        return strValue;
    }

    /**
     * 返回gb2312编码的长度，即一个中文占2个字节。
     *
     * @param str 要计算的字符串
     * @return int
     */
    public static int getGBKLen(String str) {
        final Charset gb18030 = Charset.forName("gb18030");
        return getLen(str, gb18030);
    }

    /**
     * 返回utf-8编码的长度，即一个中文占3个字节。
     *
     * @param str 要计算的字符串
     * @return int
     */
    public static int getLen(String str) {
        return getLen(str, StandardCharsets.UTF_8);
    }

    /**
     * 获取字符串长度，null返回0
     *
     * @param str     要计算的字符串
     * @param charset 计算的编码类型
     * @return int
     */
    public static int getLen(String str, Charset charset) {
        if (str == null) {
            return 0;
        }
        return str.getBytes(Optional.ofNullable(charset).orElse(StandardCharsets.UTF_8)).length;
    }

    /**
     * message格式化，用{}替代任何的对象
     *
     * @param message 待格式化字符串
     * @param objs    入参
     * @return 格式化后的字符串
     */
    public static String format(String message, Object... objs) {
        for (Object obj : objs) {
            message = formatByStr(message, obj, "{}");
        }
        return message;
    }

    /**
     * 使用指定的替换符进行格式化字符串
     *
     * @param str       待格式化字符串
     * @param obj       入参
     * @param formatStr 格式化替换字符串(默认:{})
     * @return 格式化后的字符串
     */
    public static String formatByStr(String str, Object obj, String formatStr) {
        formatStr = defaultString(formatStr, "{}");
        // 获取开始下标
        int i = str.indexOf(formatStr);
        if (i != -1) {
            return str.substring(0, i).concat(stripToEmpty(toString(obj)))
                    .concat(str.substring(i + formatStr.length()));
        } else {
            // 跳过替换
            return str;
        }
    }

    /**
     * 生成随机密码带字符
     *
     * @param pwdLen 生成密码的总长度
     * @return 密码字符串
     */
    public static String genRandomStr(int pwdLen) {
        return getRandomStr(pwdLen, STR_CHAR_ARR);
    }

    /**
     * 生成纯数字随机密码
     *
     * @param pwdLen 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String genRandomForNumStr(int pwdLen) {
        return getRandomStr(pwdLen, NUM_CHAR_ARR);
    }

    /**
     * 指定字符数组生成指定长度的随机数
     *
     * @param pwdLen  随机数长度
     * @param charArr 字符数组
     * @return String
     */
    private static String getRandomStr(int pwdLen, char[] charArr) {
        // 生成的随机数下标
        int randomIndex;
        // 生成的密码的长度
        int count = 0;

        StringBuffer pwd = new StringBuffer(EMPTY);
        SecureRandom random = new SecureRandom();
        while (count < pwdLen) {
            // 生成随机数，取绝对值，防止生成负数，
            randomIndex = Math.abs(random.nextInt(charArr.length - 1));
            if (randomIndex >= 0 && randomIndex < charArr.length) {
                pwd.append(charArr[randomIndex]);
                ++count;
            }
        }
        return pwd.toString();
    }

}
