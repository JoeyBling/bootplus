package io.github.util.spring;

import io.github.frame.prj.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author Created by 思伟 on 2020/6/6
 * @Email 2434387555@qq.com
 * @see RestGlobalExceptionHandler
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * web错误处理的配置属性
     */
    private final ErrorProperties errorProperties;

    /**
     * 由于spring实例化顺序为先执行构造方法，再注入成员变量
     * 所以可以在构造方法这样写
     */
    public GlobalExceptionHandler(@Value("${spring.application.name}") String applicationName,
                                  ServerProperties serverProperties) {
        this.errorProperties = serverProperties.getError();
    }

    /**
     * 自定义异常处理器
     * 会与默认的冲突
     *
     * @param e BaseRuntimeException
     * @return String
     */
//    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ExceptionHandler(BaseRuntimeException.class)
    public String handleMyException(BaseRuntimeException e, HttpServletRequest request) {
        log.error("系统异常出错={}", e.toString(), e);
//        return errorProperties.getPath();
        // 传入我们自己的错误状态码 4xx 5xx，否则就不会进入定制错误页面的解析流程
        /**
         * @see AbstractErrorController#getStatus(javax.servlet.http.HttpServletRequest)
         */
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 200);
        // 并不直接返回视图名称或json数据，请求转发到`BasicErrorController`，让Springboot按流程处理，从而达到自适应浏览器请求和客户端请求
        return "forward:" + errorProperties.getPath();
    }

}