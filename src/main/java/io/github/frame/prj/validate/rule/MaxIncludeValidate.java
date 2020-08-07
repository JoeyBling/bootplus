package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.StringUtils;

import java.math.BigDecimal;

/**
 * 数值大于等于校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class MaxIncludeValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object objVal = ValidateONGL.getValue(obj, param);
        if (null != objVal && new BigDecimal(StringUtils.toString(objVal)).compareTo(new BigDecimal(value)) >= 0) {
            return null != message ? message : param + "不能大于等于" + value;
        }
        return null;
    }

}

