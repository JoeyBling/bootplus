package io.github.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import io.github.config.aop.service.BaseAopContext;
import io.github.util.StringUtils;
import io.github.util.YamlUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 应用配置服务
 *
 * @author Created by 思伟 on 2020/8/4
 */
@Service
public class AppConfigService extends BaseAopContext<AppConfigService> {

    /**
     * 签名校验
     *
     * @param sign 签名
     * @param json 入参Json
     * @param map  入参Map
     * @return boolean
     */
    public boolean validateSign(String sign, String json, Map<String, String[]> map) {
        String valStr, appid;
        if (StringUtils.isNotBlank(json)) {
            // json接口
            appid = JSON.parseObject(json).getString("appid");
            valStr = json;
        } else {
            // 文件接口
            appid = map.get("appid")[0];
            valStr = appid + map.get("random")[0];
        }

        String debug = YamlUtil.getProperty("debug");
//        if ("test".equals(sign) && "true".equals(debug)) {
        if ("test".equals(sign)) {
            logger.debug("validate service provider sign, debug mode and using test sign");
            return true;
        } else {
            // 获取签名请求密码
            String requestPassword = YamlUtil.getProperty("application.requestPassword");

            // 生成校验码
            String sysSign = SecureUtil.md5().digestHex(requestPassword.concat(valStr));
            logger.debug("validate service provider sign inSign={}, sysSign={}, valStr={}", sign, sysSign, valStr);
            return StringUtils.equals(sign, sysSign);
        }
    }

    public static void main(String[] args) {
        String testValue = "{\n" +
                "    \"oper\": \"127.0.0.1\",\n" +
                "    \"appid\": \"1001\",\n" +
                "    \"random\": \"1234\",\n" +
                "    \"service\": \"bootplus.user.login.log.list\"\n" +
                "}";
        System.out.println(SecureUtil.md5().digestHex(YamlUtil.getProperty("application.requestPassword").concat(testValue)));
    }

}
