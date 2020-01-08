package io.github.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码生成器
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Configuration
public class ProducerConfig {

    @Bean
    @ConditionalOnMissingBean
    public Producer kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.put("kaptcha.border", "no");
        // 字体颜色
        properties.put("kaptcha.textproducer.font.color", "black");
        // 文字间隔
        properties.put("kaptcha.textproducer.char.space", "4");
        // 验证码文本字符长度默认为5
        properties.put("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
