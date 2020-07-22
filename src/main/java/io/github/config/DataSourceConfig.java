package io.github.config;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidSpringAopConfiguration;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.DruidPasswordCallback;
import io.github.util.db.MyDruidPasswordCallback;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

/**
 * Druid数据源配置
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
//@Import(DruidSpringAopConfiguration.class)
public class DataSourceConfig {

    /**
     * 数据库密码回调解密
     *
     * @return MyDruidPasswordCallback
     * @url https://github.com/alibaba/druid/pull/3877
     */
    @Bean
    @ConditionalOnMissingBean
    public DruidPasswordCallback myDruidPasswordCallback() {
        return new MyDruidPasswordCallback();
    }

    /**
     * 1.1.22 版本以下会被代理两次，新版本已解决
     * 参考：{ https://github.com/alibaba/druid/issues/2770 }
     * {Spring Boot二次代理问题 #3096  https://github.com/alibaba/druid/issues/3096}
     *
     * @ConditionalOnProperty(name = "spring.aop.auto",havingValue = "false")
     * @see DruidSpringAopConfiguration#advisorAutoProxyCreator()
     */
//    @Bean
    @ConditionalOnProperty(name = "spring.aop.auto", havingValue = "false")
    @Deprecated
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 注册DruidServlet（监控页面）
     * wait del：已迁移yml配置
     *
     * @return ServletRegistrationBean
     */
//    @Bean
    public ServletRegistrationBean druidServletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        // 白名单：
        // servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_ALLOW, "127.0.0.1");
        // IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        // servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_DENY, "127.0.0.1");
        // 登录查看信息的账号密码
        servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME, "admin");
        servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD, "admin");
        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_RESET_ENABLE, "false");
        return servletRegistrationBean;
    }

    /**
     * 注册DruidFilter拦截(网络url统计)
     * wait del：已迁移yml配置
     *
     * @return FilterRegistrationBean
     * @see com.alibaba.druid.spring.boot.autoconfigure.stat.DruidWebStatFilterConfiguration#webStatFilterRegistrationBean(DruidStatProperties)
     */
//    @Bean
    public FilterRegistrationBean druidFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 设置加载顺序
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<String, String>(1);
        // 设置忽略请求
        initParams.put(WebStatFilter.PARAM_NAME_EXCLUSIONS, "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
