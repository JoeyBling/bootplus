package io.github.util.config;

import com.alibaba.druid.util.DruidPasswordCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * 数据库密码回调解密
 *
 * @author Created by 思伟 on 2019/11/7
 */
@Slf4j
public class MyDruidPasswordCallback extends DruidPasswordCallback {

    /**
     * 做个缓存，防止一直请求
     */
    private String password = null;

    @Override
    public void setProperties(Properties properties) {
        if (StringUtils.isNotEmpty(password)) {
            // 程序应只在启动时调用密码解密，之后保存在内存中，不能每次使用都调用接口获取密码
            setPassword(password.toCharArray());
            return;
        }
        Assert.notNull(properties, "Properties must not be null");
        super.setProperties(properties);
        password = properties.getProperty("password");
        if (StringUtils.isNotBlank(password)) {
            try {
                // 这里的password是将配置得到的密码进行解密之后的值
                setPassword(StandAloneUtil.decrypt(password).toCharArray());
            } catch (Exception ex) {
                // 报错了不做异常抛出，有可能是本地测试密码不需要解密
                log.warn("数据库密文解密失败，跳过......");
                setPassword(password.toCharArray());
            }
        }
    }

}
