package io.github.config.filter;

import io.github.util.ConfUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义CORS跨域请求安全配置(@Order标识执行顺序 值越小越先执行)
 *
 * @author Created by 思伟 on 2019/12/17
 */
@Slf4j
@Data
@Order(1)
public class MyCorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 允许跨域访问的域名数组(使用,分隔)
        String allowOriginStr = ConfUtil.getInstance("config").getString("access.control.allow.origin");
        // 允许跨域的请求方法
        allowMethods = ConfUtil.getInstance("config").getString("access.control.allow.methods");
        // 请求的有效期,单位为秒
        maxAge = ConfUtil.getInstance("config").getString("access.control.max.age");
        // CORS请求中允许的标头
        allowHeaders = ConfUtil.getInstance("config").getString("access.control.allow.headers");
        allowOrigins = StringUtils.split(allowOriginStr, ",");
        log.debug("初始化CORS跨域请求安全配置完成===={}", this.toString());
    }

    /**
     * 只有跨域请求，或者同域时发送post请求，才会携带origin请求头
     * 而referer不论何种情况下，只要浏览器能获取到请求源都会携带
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletResponse rep = (HttpServletResponse) response;
            // 默认
            String allowOrigin = "*";
            // WEB服务器的域名/IP 地址和端口号
            String reqHost = ((HttpServletRequest) request).getHeader(HttpHeaders.HOST);
            // 包括协议和域名
            String reqOrigin = ((HttpServletRequest) request).getHeader(HttpHeaders.ORIGIN);
            // 请求的原始资源的URI
            String reqReferer = ((HttpServletRequest) request).getHeader(HttpHeaders.REFERER);
//            String url = ((HttpServletRequest) request).getRequestURL().toString();

            if (ArrayUtils.isNotEmpty(allowOrigins)) {
                // 默认使用下标为0的过滤规则
                allowOrigin = allowOrigins[0];
                for (int i = 0; i < allowOrigins.length; i++) {
                    if (StringUtils.startsWith(reqOrigin, allowOrigins[i])
                            || StringUtils.startsWith(reqReferer, allowOrigins[i])) {
                        allowOrigin = allowOrigins[i];
                    }
                }
            }
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, StringUtils.isBlank(allowMethods) ? "POST, GET, OPTIONS, DELETE" : allowMethods);
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, StringUtils.isBlank(maxAge) ? "3600" : maxAge);
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.isBlank(allowHeaders) ? "Content-Type, sign" : allowHeaders);
            // 页面只能被本站页面嵌入到iframe或者frame中(造成跨域iframe)
            rep.setHeader("X-Frame-Options", "SAMEORIGIN");
            chain.doFilter(request, rep);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * 允许跨域访问的域名数组
     */
    private String[] allowOrigins;
    /**
     * 允许跨域的请求方法
     */
    private String allowMethods;
    /**
     * 请求的有效期,单位为秒
     */
    private String maxAge;
    /**
     * CORS请求中允许的标头
     */
    private String allowHeaders;

}
