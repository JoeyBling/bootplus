package io.github.util.spring;

import io.github.frame.controller.MainSiteErrorController;
import io.github.util.exception.MyRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 *
 * @author Joey
 * @Email 2434387555@qq.com
 * @see RestGlobalExceptionHandler
 */
@Deprecated
//@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理器
     * 会与默认的冲突
     *
     * @param e MyRuntimeException
     * @return String
     */
    @ExceptionHandler(MyRuntimeException.class)
    public String handleMyException(MyRuntimeException e) {
        log.error("系统异常出错={}", e.toString(), e);
        return MainSiteErrorController.ERROR_PATH;
    }


}