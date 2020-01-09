package io.github.config.filter;

import io.github.util.ConfUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
@WebFilter(initParams = {
}, filterName = "myCorsFilter", urlPatterns = {"/*"})
public class MyCorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Assert.notNull(filterConfig, "FilterConfig must not be null");
        log.info("WebFilter->[{}] init success...", filterConfig.getFilterName());
        // 允许跨域访问的域名数组(使用,分隔)
        String allowOriginStr = ConfUtil.getInstance("configs").getString("access.control.allow.origin");
        // 允许跨域的请求方法
        allowMethods = StringUtils.defaultIfEmpty(
                ConfUtil.getInstance("config").getString("access.control.allow.methods"), DEFAULT_ALLOW_METHODS);
        // 请求的有效期,单位为秒
        maxAge = StringUtils.defaultIfEmpty(
                ConfUtil.getInstance("config").getString("access.control.max.age"), "3600");
        // CORS请求中允许的标头
        allowHeaders = StringUtils.defaultIfEmpty(
                ConfUtil.getInstance("config").getString("access.control.allow.headers"), "Content-Type, sign");
        allowOrigins = StringUtils.split(allowOriginStr, ",");
        log.info("初始化CORS跨域请求安全配置完成===={}", this.toString());
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
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowMethods);
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, maxAge);
            rep.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
            // 页面只能被本站页面嵌入到iframe或者frame中(造成跨域iframe)---X-Frame-Options
            rep.setHeader(com.google.common.net.HttpHeaders.X_FRAME_OPTIONS, "SAMEORIGIN");
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

    /**
     * 默认允许跨域的请求方法数组
     */
    protected final static String[] DEFAULT_ALLOW_METHODS_ARRAY = ArrayUtils.toArray(
            HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name());

    /**
     * 默认允许跨域的请求方法
     */
    protected final static String DEFAULT_ALLOW_METHODS = StringUtils.join(DEFAULT_ALLOW_METHODS_ARRAY, ", ");

}
