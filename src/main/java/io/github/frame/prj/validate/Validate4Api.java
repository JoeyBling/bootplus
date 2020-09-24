package io.github.frame.prj.validate;

import com.alibaba.fastjson.JSONArray;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.frame.prj.validate.rule.*;
import io.github.util.StringUtils;
import io.github.frame.prj.exception.SysRuntimeException;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Api入参校验工具类
 * <p>
 * TODO 考虑是否放弃ognl语法解析,使用缓存+反射的方式,或者使用SpEL，Validate4JQuery也需要一同处理
 *
 * @author Updated by 思伟 on 2020/8/6
 */
public class Validate4Api {
    /**
     * 校验规则
     */
    List<Validate4ApiRule> rules;

    public Validate4Api() {
        super();
    }

    public Validate4Api(List<Validate4ApiRule> rules) {
        this.rules = rules;
    }

    public static void valid(Object t, List<Validate4ApiRule> rules) {
        new Validate4Api(rules).valid(t);
    }

    public static TransBaseResponse valid2Response(Object t, List<Validate4ApiRule> rules) {
        try {
            valid(t, rules);
        } catch (SysRuntimeException e) {
            return TransBaseResponse.builder()
                    .code(ResponseCodeConst.ERROR_VALIDATE).msg(e.getMessage()).build();
        }
        return null;
    }

    public static <T> void valid(T t, List<Validate4ApiRule> rules, Function<T, String> customValid) {
        valid(t, rules);
        if (null != customValid) {
            String errorMsg = customValid.apply(t);
            if (StringUtils.isNotEmpty(errorMsg)) {
                throw new SysRuntimeException(ResponseCodeConst.ERROR_VALIDATE, errorMsg);
            }
        }
    }

    public static <T> TransBaseResponse valid2Response(T t, List<Validate4ApiRule> rules, Function<T, String> customValid) {
        TransBaseResponse response = valid2Response(t, rules);
        if (null == response) {
            return Optional.ofNullable(customValid.apply(t))
                    .map(errorMsg -> TransBaseResponse.builder().code(ResponseCodeConst.ERROR_VALIDATE).msg(errorMsg).build())
                    .orElse(null);
        }
        return response;
    }

    public void valid(Object t) throws SysRuntimeException {
        String rltMessage = "";
        if (null == rules) {
            return;
        }

        for (Validate4ApiRule rule : rules) {
            rltMessage = doValid(t, rule.getParam(), rule.getParamName(),
                    rule.getRuleType(), rule.getRuleValue());

            if (null != rltMessage) {
                throw new SysRuntimeException(ResponseCodeConst.ERROR_VALIDATE, rltMessage);
            }
        }
    }

    /**
     * 执行校验
     */
    private String doValid(Object t, String param, String paramName, ValidateEnum ruleType, String ruleValue) {
        if (null == t) {
            return null;
        } else if (StringUtils.indexOf(param, ".") != -1) {
            // 判断是否有. 调用子对象解析
            return doValidSubObject(t, param, paramName, ruleType, ruleValue);
        } else if (t.getClass().isArray()) {
            // 判断t是否是数组
            return doValidDetails(CollectionUtils.arrayToList(t), param, paramName, ruleType, ruleValue);
        } else if (t instanceof Collection) {
            // 判断t是否集合
            return doValidDetails((Collection<?>) t, param, paramName, ruleType, ruleValue);
        } else if (t instanceof Map) {
            // 判断是否map
            throw new SysRuntimeException(ResponseCodeConst.ERROR_VALIDATE, "不支持解析Map对象");
        } else {
            // 调用execValid
            return execValid(t, param, paramName, ruleType, ruleValue);
        }
    }

    /**
     * 校验子对象
     */
    private String doValidSubObject(Object t, String param, String paramName, ValidateEnum ruleType, String ruleValue) {
        String leftParam = StringUtils.left(param, param.indexOf("."));
        String rightParam = StringUtils.right(param, param.length() - param.indexOf(".") - 1);
        Object subObject = ValidateONGL.getValue(t, leftParam);
        return doValid(subObject, rightParam, paramName, ruleType, ruleValue);
    }

    /**
     * 校验List数组
     */
    private String doValidDetails(Collection<?> details, String param, String paramName, ValidateEnum ruleType, String ruleValue) {
        String rltMessage;
        for (Object detail : details) {
            rltMessage = doValid(detail, param, paramName, ruleType, ruleValue);
            if (StringUtils.isNotBlank(rltMessage)) {
                return rltMessage;
            }
        }
        return null;
    }

    /**
     * 执行校验
     */
    private String execValid(Object obj, String param, String paramName, ValidateEnum ruleType, String ruleValue) {
        try {
            List<String> array;
            switch (ruleType) {
                case required:
                    return RequiredValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能为空", paramName));
                case enums:
                    return EnumsValidate.exec(obj, param, ruleValue, StringUtils.format("{} 只允许 [{}]", paramName, ruleValue));
                case require_from_group:
                    return RequireFromGroupValidate.exec(obj, param, ruleValue, null);
                case minlength:
                    return MinLengthValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能小于 {} 个字符", paramName, ruleValue));
                case maxlength:
                    return MaxLengthValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能大于 {} 个字符", paramName, ruleValue));
                case range_length:
                    array = JSONArray.parseArray(ruleValue, String.class);
                    return RangeLengthValidate.exec(obj, param, ruleValue, StringUtils.format("{} 长度必须介于 {} 和 {} 个字符之间", paramName, array.get(0), array.get(1)));
                case min:
                    return MinExcludeValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能小于 {}", paramName, ruleValue));
                case min_include:
                    return MinIncludeValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能小于等于 {}", paramName, ruleValue));
                case max:
                    return MaxExcludeValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能大于 {}", paramName, ruleValue));
                case max_include:
                    return MaxIncludeValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不能大于等于 {}", paramName, ruleValue));
                case range:
                    array = JSONArray.parseArray(ruleValue, String.class);
                    return RangeValidate.exec(obj, param, ruleValue, StringUtils.format("{} 必须介于 {} 和 {} 之间", paramName, array.get(0), array.get(1)));
                case number:
                    return NumberValidate.exec(obj, param, ruleValue, StringUtils.format("{} 必须是数字", paramName));
                case digits:
                    return DigitsValidate.exec(obj, param, ruleValue, StringUtils.format("{} 必须是0或正整数", paramName));
                case email:
                    return EmailValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不是有效的email地址", paramName));
                case mobile:
                    return MobileValidate.exec(obj, param, ruleValue, StringUtils.format("{} 不是有效的手机号", paramName));
                case remote:
                case remoteJava:
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new SysRuntimeException(ResponseCodeConst.ERROR_VALIDATE, StringUtils.format("数据校验规则读取错误, param={}, ruleType={}, ruleValue={}", param, ruleType, ruleValue));
        }
    }

}
