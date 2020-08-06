package io.github.frame.prj.validate.rule;

import com.alibaba.fastjson.JSONArray;

/**
 * 分组必填校验
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class RequireFromGroupValidate {

    public static String exec(Object obj, String param, String value, String message) {
        JSONArray array = JSONArray.parseArray(value);
        // 最低必填数量
        int requireCount = array.getInteger(0);
        // 空白字符串总数
        int blankCount = 0;
        array.set(0, param);
        for (Object _param : array) {
            String idParam = _param.toString();
            idParam = idParam.replace("#", "");
            blankCount += null == (RequiredValidate.exec(obj, idParam, "true", null)) ? 0 : 1;
        }
        if (requireCount > array.size() - blankCount) {
            return null != message ? message : array.toString() + "必填" + requireCount + "个";
        }
        return null;
    }

}