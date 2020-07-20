package io.github.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 自定义security
 *
 * @author Created by 思伟 on 2020/6/5
 */
@Configuration
@EnableWebSecurity
public class ActuatorWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.endpoints.web.base-path:/monitor/*}")
    private String actuatorPath;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/statics/*", "/js/*");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        // 对actuator监控所用的访问全部需要认证
        http
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class))
                .hasRole("ADMIN")
                .requestMatchers(EndpointRequest.toAnyEndpoint())
                .permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                // swagger页面需要添加登录校验
                .antMatchers("/swagger-ui.html").authenticated()
                .antMatchers("/")
                .permitAll()
                //普通的接口不需要校验
                .antMatchers("/**").permitAll()
                .anyRequest().permitAll().and().csrf().disable().headers().frameOptions().disable()
                .and()
                .formLogin();
    }

    @Override
    public void init(WebSecurity web) throws Exception {
        super.init(web);
    }
}
