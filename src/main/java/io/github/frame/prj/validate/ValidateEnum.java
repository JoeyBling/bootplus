package io.github.frame.prj.validate;

import io.github.common.enums.IEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验类型枚举值
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public enum ValidateEnum implements IEnum {
    required("required", "必填"),
    enums("enums", "限定取值范围"),
    require_from_group("require_from_group", "分组必填"),
    minlength("minlength", "字符串最短限制"),
    maxlength("maxlength", "字符串最长限制"),
    range_length("range_length", "字符串长度范围"),
    min("min_exclude", "数字最小值(不含)"),
    min_include("min_include", "数字最小值(含)"),
    max("max_exclude", "数字最大值(不含)"),
    max_include("max_include", "数字最大值(含)"),
    range("range", "数字范围"),
    number("number", "数字"),
    digits("digits", "正整数"),
    remote("remote", "远程调用"),
    remoteJava("remote_java", "本地调用"),
    email("email", "email"),
    mobile("mobile", "手机号");

    private String key;
    private String value;

    private ValidateEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public static ValidateEnum getByKey(String key) {
        ValidateEnum[] es = values();

        for (int i = 0; i < es.length; ++i) {
            if (es[i].getKey().equals(key)) {
                return es[i];
            }
        }

        return null;
    }

    public static String[] toArray(List<ValidateEnum> enumList) {
        if (null != enumList && !enumList.isEmpty()) {
            String[] status = new String[enumList.size()];

            for (int i = 0; i < enumList.size(); ++i) {
                status[i] = enumList.get(i).getKey();
            }

            return status;
        } else {
            return null;
        }
    }

    public static List<ValidateEnum> fromArray(String[] typeNameList) {
        List<ValidateEnum> enumList = new ArrayList();
        ValidateEnum e = null;

        for (int i = 0; i < typeNameList.length; ++i) {
            e = getByKey(typeNameList[i]);
            if (null != e) {
                enumList.add(e);
            }
        }

        return enumList;
    }
}
