package io.github.util.spring;

import io.github.util.R;
import io.github.util.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 添加全局异常处理流程，根据需要设置需要处理的异常
 *
 * @author Created by 思伟 on 2020/6/6
 * @see org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
 */
@RestControllerAdvice
@Slf4j
public class RestGlobalExceptionHandler {

    /**
     * 自定义异常处理器
     * TODO org.springframework.web.HttpMediaTypeNotAcceptableException:
     * Could not find acceptable representation
     *
     * @param e BaseRuntimeException
     * @return Map
     * @see GlobalExceptionHandler#handleMyException
     * @since 2.3.1
     */
    @Deprecated
    @ExceptionHandler(BaseRuntimeException.class)
    public R handleMyException(BaseRuntimeException e, HttpServletRequest request) {
        return R.error(e.getCode(), e.getMsg());
    }

    /**
     * 数据库中已存在该记录
     *
     * @param e DuplicateKeyException
     * @return Map
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    /**
     * 数据超长等约束...
     *
     * @param e DataIntegrityViolationException
     * @return R
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public R handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return R.error();
    }

    /**
     * 没有权限
     *
     * @param e AuthorizationException
     * @return Map
     */
    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e) {
        log.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    /**
     * 默认异常处理
     *
     * @param request   HttpServletRequest
     * @param exception Throwable
     * @return Map
     */
    @ExceptionHandler(Throwable.class)
    public Object defaultErrorHandler(HttpServletRequest request, Throwable exception) {
        // 记录异常日志
        log.error(exception.getMessage(), exception);
        return R.error(exception.getMessage());
    }

}