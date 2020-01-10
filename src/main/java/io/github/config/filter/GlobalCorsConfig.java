package io.github.config.filter;

import io.github.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 使用CORS，用于解决ajax跨域访问问题（不想用原生SpringBoot提供的，自己实现了一个）
 *
 * @author Created by 思伟 on 2019/12/25
 * @ConditionalOnBean //   当给定的在bean存在时,则实例化当前Bean
 * @ConditionalOnMissingBean //   当给定的在bean不存在时,则实例化当前Bean
 * @ConditionalOnClass //   当给定的类名在类路径上存在，则实例化当前Bean
 * @ConditionalOnMissingClass //   当给定的类名在类路径上不存在，则实例化当前Bean
 * @see MyCorsFilter
 */
@Configuration
@Slf4j
@Deprecated
public class GlobalCorsConfig {

    private final static String missingClassPath = ClassUtil.getClass(MyCorsFilter.class);

    /**
     * 如果需要开启 删除@ConditionalOnMissingClass 注解即可，然后注释 MyCorsFilter 类的 @WebFilter 注解
     *
     * @ConditionalOnMissingClass 当某些类不存在于 classpath 上时候才创建某个Bean
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass({"io.github.config.filter.MyCorsFilter"})
    public CorsConfiguration buildCorsConfig() {
        log.info("跨域拦截配置 init success...");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        return corsConfiguration;
    }

    /**
     * 如果想要对某一接口配置 CORS，可以在方法上或者类上添加 @CrossOrigin 注解
     *
     * @param corsConfiguration
     * @return CorsFilter
     * @ConditionalOnBean 表示当给定的在bean存在时, 则实例化当前Bean
     * @see org.springframework.web.bind.annotation.CrossOrigin
     */
    @Bean
    @ConditionalOnBean(CorsConfiguration.class)
    public CorsFilter corsFilter(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}