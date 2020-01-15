package io.github.base;

import io.github.config.filter.MyCorsFilter;
import io.github.config.listener.ContextListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解决SpringBoot junit测试导致的全局过滤器和监听器失效
 * 事实发现，并没什么用（应该就是测试不会去注册）
 *
 * @author Created by 思伟 on 2020/1/15
 */
@Configuration
public class WebConfig {

    /**
     * 手动注册过滤器
     */
    @Bean
    public FilterRegistrationBean thirdFilter() {
        MyCorsFilter corsFilter = new MyCorsFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(corsFilter);
//        filterRegistrationBean.setUrlPatterns(urls);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 手动注册监听器
     */
    @Bean
    public ServletListenerRegistrationBean listenerRegister() {
        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
        srb.setListener(new ContextListener());
        return srb;
    }

}
