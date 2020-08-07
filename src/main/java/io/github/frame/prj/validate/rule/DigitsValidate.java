package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.StringUtils;

/**
 * 正整数及0校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class DigitsValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object validValue = ValidateONGL.getValue(obj, param);
        if (isValid(value) && null != validValue
                && !StringUtils.isNumeric(String.valueOf(validValue))) {
            return null != message ? message : param + "必须是0或正整数";
        }
        return null;
    }

    protected static boolean isValid(String ruleValue) {
        return StringUtils.equalsIgnoreCase("true", ruleValue);
    }

}