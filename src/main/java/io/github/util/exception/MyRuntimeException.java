package io.github.util.exception;

/**
 * 自定义异常抽象类
 *
 * @author Created by 思伟 on 2020/4/10
 */
public abstract class MyRuntimeException extends RuntimeException implements MyException {

    public MyRuntimeException(String message) {
        super(message);
    }

    public MyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyRuntimeException(Throwable cause) {
        super(cause);
    }

}
