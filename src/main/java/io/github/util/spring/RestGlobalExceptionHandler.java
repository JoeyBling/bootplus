package io.github.util.spring;

import io.github.util.R;
import io.github.util.exception.MyRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author Joey
 * @Email 2434387555@qq.com
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
     * @param e MyRuntimeException
     * @return Map
     */
    @ExceptionHandler(MyRuntimeException.class)
    public R handleMyException(MyRuntimeException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMsg());
        return r;
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
     * 添加全局异常处理流程，根据需要设置需要处理的异常
     *
     * @param request   HttpServletRequest
     * @param exception Exception
     * @return Map
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public Object methodArgumentNotValidHandler(HttpServletRequest request, Exception exception) throws Exception {
        // 记录异常日志
        log.error(exception.getMessage(), exception);
        return R.error();
    }

}