package io.github.frame.prj.validate;

import com.alibaba.fastjson.JSON;
import io.github.util.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 校验规则
 *
 * @author Updated by 思伟 on 2020/8/6
 */
@Data
public class Validate4ApiRule {
    /**
     * 参数名
     */
    private String param;
    /**
     * 参数含义
     */
    private String paramName;
    /**
     * 校验类型
     */
    private ValidateEnum ruleType;
    /**
     * 校验值
     */
    private String ruleValue;

    /**
     * 必填
     */
    public static Validate4ApiRule required(String param, String paramName) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.required, "true");
    }

    /**
     * 枚举
     */
    public static Validate4ApiRule enums(String param, String paramName, String[] enumValue) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.enums, StringUtils.joinWith(",", enumValue));
    }

    /**
     * 组合必填(不能全部为空)
     */
    public static Validate4ApiRule requireFromGroup(String[] params, int minRequireCount) {
        String param = params[0];
        params[0] = String.valueOf(minRequireCount);
        return new Validate4ApiRule(param, null, ValidateEnum.require_from_group, JSON.toJSONString(params));
    }

    /**
     * 最小长度
     */
    public static Validate4ApiRule minlength(String param, String paramName, int length) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.minlength, String.valueOf(length));
    }

    /**
     * 最大长度
     */
    public static Validate4ApiRule maxlength(String param, String paramName, int length) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.maxlength, String.valueOf(length));
    }

    /**
     * 长度范围
     */
    public static Validate4ApiRule rangeLength(String param, String paramName, int minLength, int maxLength) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.range_length, JSON.toJSONString(new Integer[]{minLength, maxLength}));
    }

    /**
     * 最小值
     */
    public static Validate4ApiRule min(String param, String paramName, String minValue) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.min, minValue);
    }

    /**
     * 最小值(含)
     */
    public static Validate4ApiRule minInclude(String param, String paramName, String minValue) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.min_include, minValue);
    }

    /**
     * 最大值
     */
    public static Validate4ApiRule max(String param, String paramName, String maxValue) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.max, StringUtils.toString(maxValue));
    }

    /**
     * 最大值(含)
     */
    public static Validate4ApiRule maxInclude(String param, String paramName, String maxValue) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.max_include, maxValue);
    }

    /**
     * 数字范围
     */
    public static Validate4ApiRule range(String param, String paramName, String minValue, String maxValue) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.range, JSON.toJSONString(new String[]{minValue, maxValue}));
    }

    /**
     * 数字
     */
    public static Validate4ApiRule number(String param, String paramName) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.number, "true");
    }

    /**
     * 正整数
     */
    public static Validate4ApiRule digits(String param, String paramName) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.digits, "true");
    }

    /**
     * 邮件地址
     */
    public static Validate4ApiRule email(String param, String paramName) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.email, "true");
    }

    /**
     * 手机号
     */
    public static Validate4ApiRule mobile(String param, String paramName) {
        return new Validate4ApiRule(param, paramName, ValidateEnum.mobile, "true");
    }

    /**
     * 唯一构造函数
     */
    protected Validate4ApiRule(String param, String paramName, ValidateEnum ruleType, String ruleValue) {
        this.param = param;
        this.paramName = paramName;
        this.ruleType = ruleType;
        this.ruleValue = ruleValue;
    }

}
