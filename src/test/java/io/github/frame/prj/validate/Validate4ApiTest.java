package io.github.frame.prj.validate;

import com.google.common.collect.Lists;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.frame.prj.constant.UserTypeConst;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.util.StringUtils;
import io.github.frame.prj.exception.SysRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Api入参校验测试
 *
 * @author Created by 思伟 on 2020/8/6
 */
@RunWith(MockitoJUnitRunner.Silent.class)
class Validate4ApiTest {

    /**
     * 模拟请求
     */
    TestRequest request;
    /**
     * 校验规则
     */
    ArrayList<Validate4ApiRule> rules;

    @BeforeEach
    public void setUp() throws Exception {
        // init data
        request = TestRequest.builder()
                .str("test str").str2("测试组合必填")
                .enums(UserTypeConst.APP)
                .minlength("12345").maxlength("1234567891").rangeLength("12345678")
                .min(11).minBigDecimal(new BigDecimal("11")).minInclude(11)
                .max(10).maxInclude(10).range(50)
                .number("15.212").digits("0").email("2434387555@qq.com").mobile("13647910412")
                .build();
        rules = Lists.newArrayList(
                Validate4ApiRule.required("str", "字符串"),
                Validate4ApiRule.enums("enums", "用户类型", UserTypeConst.CONST_MAP.keySet().toArray(new String[0])),
                Validate4ApiRule.requireFromGroup(new String[]{"str", "str2", "str3"}, 2),
                Validate4ApiRule.minlength("minlength", "最小长度测试", 5),
                Validate4ApiRule.maxlength("maxlength", "最大长度测试", 10),
                Validate4ApiRule.rangeLength("rangeLength", "长度范围测试", 5, 10),
                Validate4ApiRule.min("min", "最小值测试", 10),
                Validate4ApiRule.min("minBigDecimal", "最小值BigDecimal测试", new BigDecimal("10")),
                Validate4ApiRule.minInclude("minInclude", "小于等于测试", 10),
                Validate4ApiRule.max("max", "最大值测试", 10),
                Validate4ApiRule.maxInclude("maxInclude", "大于等于测试", 11),
                Validate4ApiRule.range("range", "数字范围测试", new BigDecimal("10.5"), new BigDecimal("50.1")),
                Validate4ApiRule.number("number", "数字测试"),
                Validate4ApiRule.digits("digits", "正整数测试"),
                Validate4ApiRule.email("email", "邮件地址测试"),
                Validate4ApiRule.mobile("mobile", "手机号测试"),
                new Validate4ApiRule("remote", "远程调用测试", ValidateEnum.remote, "true"),
                new Validate4ApiRule("remoteJava", "本地调用测试", ValidateEnum.remoteJava, "true")
        );
    }

    @Test
    void valid() {
        Validate4Api.valid(request, rules);
        Assert.assertThrows(SysRuntimeException.class, () -> {
            Validate4Api.valid(request.setEnums("error"), rules);
        });
    }

    @Test
    void testValid() {
        Validate4Api.valid(request, rules, null);
        Assert.assertThrows(SysRuntimeException.class, () -> {
            Validate4Api.valid(request, rules, req -> {
                if (TestRequest.class.isAssignableFrom(req.getClass())) {
                    return "自定义回调错误";
                }
                return null;
            });
        });
    }

    @Test
    void valid2Response() {
        Assert.assertNull(Validate4Api.valid2Response(request, rules));
        Assert.assertEquals(TransBaseResponse.builder().code(ResponseCodeConst.ERROR_VALIDATE)
                        .msg(StringUtils.format("{} 只允许 [{}]", "用户类型",
                                StringUtils.joinWith(",",
                                        UserTypeConst.CONST_MAP.keySet().toArray(new Object[0])))).build(),
                Validate4Api.valid2Response(request.setEnums("error"), rules));
    }

    @Test
    void testValid2Response() {
        final String errorMsg = "自定义回调错误";
        Assert.assertNull(Validate4Api.valid2Response(request, rules, req -> {
            if (req.getEnums().equals(UserTypeConst.SYS)) {
                return errorMsg;
            }
            return null;
        }));
        Assert.assertEquals(TransBaseResponse.builder().code(ResponseCodeConst.ERROR_VALIDATE).msg(errorMsg).build(),
                Validate4Api.valid2Response(request.setEnums(UserTypeConst.ADM), rules, req -> {
                    if (req.getEnums().equals(UserTypeConst.ADM)) {
                        return errorMsg;
                    }
                    return null;
                }));
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class TestRequest {
        /**
         * 字符串必填
         */
        private String str;

        /**
         * 组合必填
         */
        private String str2;

        /**
         * 组合必填
         */
        private String str3;

        /**
         * 枚举
         */
        private String enums;

        /**
         * 最小长度
         */
        private String minlength;

        /**
         * 最大长度
         */
        private String maxlength;

        /**
         * 长度范围
         */
        private String rangeLength;

        /**
         * 最小值
         */
        private Integer min;

        /**
         * 最小值
         */
        private BigDecimal minBigDecimal;

        /**
         * 小于等于
         */
        private int minInclude;

        /**
         * 最大值
         */
        private int max;

        /**
         * 大于等于
         */
        private int maxInclude;

        /**
         * 数字范围
         */
        private int range;

        /**
         * 数字
         */
        private String number;

        /**
         * 正整数
         */
        private String digits;

        /**
         * 邮件地址
         */
        private String email;

        /**
         * 手机号
         */
        private String mobile;

    }


}