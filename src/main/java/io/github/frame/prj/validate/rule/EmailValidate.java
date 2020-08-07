package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class EmailValidate {

    static Pattern pattern = Pattern.compile("^[A-Za-z0-9\\.\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    public static String exec(Object obj, String param, String value, String message) {
        Object validValue = ValidateONGL.getValue(obj, param);
        if ("true".equalsIgnoreCase(value) && null != validValue) {
            Matcher matcher = pattern.matcher(StringUtils.toString(validValue));
            if (!matcher.matches()) {
                return null != message ? message : param + "不是有效的email地址";
            }
        }
        return null;
    }

}