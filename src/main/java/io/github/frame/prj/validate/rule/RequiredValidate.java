package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.StringUtils;

/**
 * 必填校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class RequiredValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object objValue = ValidateONGL.getValue(obj, param);
        if ("true".equalsIgnoreCase(value) && StringUtils.isBlank(StringUtils.toString(objValue))) {
            return null != message ? message : param + "不能为空";
        }
        return null;
    }

}