package io.github.common.hessian;

import io.github.frame.constant.SystemConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * 自定义的HessianServiceExporter
 * 会将客户端的请求request放到ThreadLocal中，在service实现中，看一看通过HessianContext获取请求的request对象
 * 通过自定义注解发布Hessian服务:{ https://blog.csdn.net/myth_g/article/details/88041459 }
 *
 * @author Created by 思伟 on 2020/7/10
 * @since 4.x
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
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                final String element = enumeration.nextElement();
                log.debug("Hessian request get headerName[{}],value[{}]", element,
                        URLDecoder.decode(request.getHeader(element), SystemConst.DEFAULT_CHARSET.name()));
            }
//        String authorization = request.getHeader("Authorization");
            log.debug("Hessian请求访问[{}],请求主机：[{}]", request.getRequestURI(), request.getHeader("host"));
            // 保存Request到Hessian线程上下文
            HessianContext.setRequest(request);
            invoke(request.getInputStream(), response.getOutputStream());
        } catch (Throwable ex) {
            throw new NestedServletException("Hessian skeleton invocation failed", ex);
        } finally {
            HessianContext.clear();
        }
    }

    /*@Override
    public void invoke(InputStream inputStream, OutputStream outputStream) throws Throwable {
        Assert.notNull(this.skeleton, "Hessian exporter has not been initialized");
        doInvoke(this.skeleton, inputStream, outputStream);
    }*/

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        log.debug("The Hessian serviceInterface [{}] " +
                        "use the implementation class [{}] to load complete!",
                getServiceInterface().getSimpleName(), this.getService());
    }

}
