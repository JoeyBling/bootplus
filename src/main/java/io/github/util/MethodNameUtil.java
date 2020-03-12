package io.github.util;

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

    public static void main(String[] args) {
        System.out.println(getMethodName(null));
        System.out.println(getMethodName("a"));
        System.out.println(getMethodName("abcd"));
    }

}
