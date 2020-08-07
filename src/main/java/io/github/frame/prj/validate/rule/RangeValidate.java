package io.github.frame.prj.validate.rule;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * 数值范围校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class RangeValidate {

    public static String exec(Object obj, String param, String value, String message) {
        List<String> array = JSONArray.parseArray(value, String.class);
        if (null != MinExcludeValidate.exec(obj, param, array.get(0), null)
                || null != MaxExcludeValidate.exec(obj, param, array.get(1), null)) {
            return null != message ? message : param + "必须介于" + array.get(0) + "和" + array.get(1) + "之间";
        }
        return null;
    }
}