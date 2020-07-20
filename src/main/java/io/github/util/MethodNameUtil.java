package io.github.util;

import com.google.common.base.CaseFormat;

/**
 * 方法名帮助类
 *
 * @author Created by 思伟 on 2020/3/12
 */
public class MethodNameUtil {

    /**
     * 获取getter setter的名称，即首字母大写形式 <p>
     * 注意：AAA -> AAA; aAa -> aAa; aaa -> Aaa; Aaa -> Aaa;
     *
     * @param name
     * @return
     */
    public static String getMethodName(String name) {
        int minLength = 2;
        if (StringUtils.isNotEmpty(name) &&
                StringUtils.length(name) >= minLength) {
            char c1 = name.charAt(0);
            char c2 = name.charAt(1);
            if (Character.isUpperCase(c2)) {
                return name;
            } else {
                return Character.toUpperCase(c1) + name.substring(1);
            }
        }
        return name;
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param c 原字符串
     * @return String
     */
    public static String camel2underStr(String c) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, c);
//        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, c);
    }

    public static void main(String[] args) {
        System.out.println(getMethodName(null));
        System.out.println(getMethodName("a"));
        System.out.println(getMethodName("abcd"));
        System.out.println(camel2underStr("loginIp"));
    }

}
