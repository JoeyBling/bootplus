package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.RegexUtil;
import io.github.util.StringUtils;

/**
 * 手机号校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class MobileValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object validValue = ValidateONGL.getValue(obj, param);
        if ("true".equalsIgnoreCase(value) && null != validValue) {
            if (!RegexUtil.validMobile(StringUtils.toString(validValue))) {
                return null != message ? message : param + "不是有效的手机号";
            }
        }
        return null;
    }

}