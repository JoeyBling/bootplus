package io.github.common.hessian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的HessianServiceExporter
 * 会将客户端的请求request放到ThreadLocal中，在service实现中，看一看通过HessianContext获取请求的request对象
 *
 * @author Created by 思伟 on 2020/7/10
 */
@Slf4j
public class HessianServiceExporter extends org.springframework.remoting.caucho.HessianServiceExporter {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (!"POST".equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[]{"POST"}, "HessianServiceExporter only supports POST requests");
        }
        response.setContentType(CONTENT_TYPE_HESSIAN);
        try {
            // 保存Request到Hessian线程上下文
            HessianContext.setRequest(request);
            invoke(request.getInputStream(), response.getOutputStream());
        } catch (Throwable ex) {
            throw new NestedServletException("Hessian skeleton invocation failed", ex);
        } finally {
            HessianContext.clear();
        }
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        log.debug("The Hessian serviceInterface [{}] " +
                        "use the implementation class [{}] to load complete！",
                getServiceInterface().getSimpleName(), this.getService());
    }
}
