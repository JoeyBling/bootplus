package io.github.frame.prj.validate.rule;

import com.alibaba.fastjson.JSONArray;
import io.github.frame.prj.validate.ValidateONGL;

import java.util.List;

/**
 * 字符串长度范围校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class RangeLengthValidate {

    public static String exec(Object obj, String param, String value, String message) {
        List<String> array = JSONArray.parseArray(value, String.class);
        Object val = ValidateONGL.getValue(obj, param);
        if (null != MinLengthValidate.exec(obj, param, array.get(0), null)
                || null != MaxLengthValidate.exec(obj, param, array.get(1), null)) {
            return null != message ? message : param + "长度必须介于" + array.get(0) + "和" + array.get(1) + "个字符之间";
        }
        return null;
    }

}