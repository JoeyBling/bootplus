package io.github.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验工具类
 *
 * @author Created by 思伟 on 2020/3/18
 */
public class RegexUtil {

    /**
     * 手机号校验正则表达式
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    /**
     * 换行正则表达式对象
     */
    private static final Pattern CRLF_PATTERN = Pattern.compile("(\r\n|\r|\n|\n\r)");

    /**
     * 校验是否是手机号
     *
     * @param mobile 手机号
     * @return boolean
     */
    public static boolean validMobile(final String mobile) {
        if (StringUtils.isNotEmpty(mobile)) {
            return mobile.matches(REGEX_MOBILE);
        }
        return false;
    }

    /**
     * 替换字符串中的回车换行符
     *
     * @param str         原字符串
     * @param replacement 替换回车换行符的字符串
     * @return 替换后字符串
     */
    public static String cleanLineBreak(String str, final String replacement) {
        Matcher m = CRLF_PATTERN.matcher(str);
        if (m.find()) {
            return m.replaceAll(Optional.ofNullable(replacement).orElse(StringUtils.EMPTY));
        }
        return str;
    }

    /**
     * @see #cleanLineBreak(String, String)
     */
    public static String cleanLineBreak(String str) {
        return cleanLineBreak(str, StringUtils.EMPTY);
    }

    /**
     * 将String中的所有pattern匹配的字串替换掉
     *
     * @param string      待替换的字符串
     * @param replacement 替换接口实现
     * @return 替换后的文本
     * @see Matcher#replaceAll(java.lang.String)
     */
    public static String replaceAll(String string, ReplaceCallBack replacement) {
        if (string == null || null == replacement) {
            return null;
        }
        Matcher m = replacement.getPattern().matcher(string);
        if (m.find()) {
            // 正则匹配通过、找到对应模板
            StringBuffer sb = new StringBuffer();
            int index = 0;
            do {
                m.appendReplacement(sb, replacement.replace(m.group(0), index++, m));
            } while (m.find());
            m.appendTail(sb);
            return sb.toString();
        }
        return string;
    }


    /**
     * 字符串替换回调接口
     *
     * @author Created by 思伟 on 2020/7/29
     */
    public interface ReplaceCallBack {

        /**
         * 替换查找的正则表达式对象
         *
         * @return Pattern
         */
        Pattern getPattern();

        /**
         * 返回替换后的文本
         *
         * @param text    匹配通过的字符串
         * @param index   替换的次序(从0开始)
         * @param matcher Matcher对象
         * @return 替换后的文本（不要出现特殊字符）
         */
        String replace(String text, int index, Matcher matcher);

    }

}
