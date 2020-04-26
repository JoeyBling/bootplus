package io.github.util.exception;

/**
 * 自定义异常抽象类
 *
 * @author Created by 思伟 on 2020/4/10
 */
public abstract class BaseRuntimeException extends RuntimeException implements MyException {

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

}
