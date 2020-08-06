package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;

import java.math.BigDecimal;

/**
 * 数值类型校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class NumberValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object validValue = ValidateONGL.getValue(obj, param);
        if ("true".equalsIgnoreCase(value) && null != validValue) {
            try {
                new BigDecimal(String.valueOf(validValue));
            } catch (NumberFormatException e) {
                return null != message ? message : param + "必须是数字";
            }
        }
        return null;
    }
}