package io.github.util.spring;

import io.github.util.R;
import io.github.util.RRException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常处理器
     *
     * @param e RRException
     * @return Map
     */
    @ExceptionHandler(RRException.class)
    public R handleRRException(RRException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
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
        logger.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    /**
     * 没有权限
     *
     * @param e AuthorizationException
     * @return Map
     */
    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e) {
        logger.error(e.getMessage(), e);
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
    @ResponseBody
    public Object methodArgumentNotValidHandler(HttpServletRequest request, Exception exception) throws Exception {
        // 记录异常日志
        logger.error(exception.getMessage(), exception);
        return R.error();
    }

}