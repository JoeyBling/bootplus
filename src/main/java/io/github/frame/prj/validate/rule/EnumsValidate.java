package io.github.frame.prj.validate.rule;

import io.github.frame.prj.validate.ValidateONGL;
import io.github.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举值校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class EnumsValidate {

    public static String exec(Object obj, String param, String value, String message) {
        Object objVal = ValidateONGL.getValue(obj, param);
        if (null != objVal) {
            List<String> values = Arrays.stream(value.split(",")).map(StringUtils::trim).collect(Collectors.toList());
            if (!StringUtils.collectionIn(StringUtils.toString(objVal), values)) {
                return null != message ? message : param + "可选值为" + StringUtils.toString(values);
            }
        }
        return null;
    }
}