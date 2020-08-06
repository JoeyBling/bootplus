package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.StringUtils;

/**
 * 字符串长度校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class MinLengthValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object val = ValidateONGL.getValue(obj, param);
        if (null != val && StringUtils.toString(val).length() < Integer.valueOf(value)) {
            return null != message ? message : param + "不能小于" + value + "个字符";
        }
        return null;
    }

}