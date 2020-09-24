package io.github.frame.prj.exception;

/**
 * 自定义异常抽象类
 * 系统中的异常类都要继承自这个父类
 * 运行时异常 {@link RuntimeException}
 *
 * @author Created by 思伟 on 2020/4/10
 */
public abstract class BaseRuntimeException extends RuntimeException implements MyException {

    /**
     * 非`jdk>1.8`需要抽象类实现
     *
     * @return {@link #DEFAULT_ERROR_CODE}
     * @since 1.7
     */
    @Override
    public int getErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

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
