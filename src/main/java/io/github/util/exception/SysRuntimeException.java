package io.github.util.exception;

/**
 * 自定义异常类
 * 通常，Java的异常(包括Exception和Error)分为可查的异常（checked exceptions）和不可查的异常（unchecked exceptions）。
 * 可查异常（编译器要求必须处置的异常）：正确的程序在运行中，很容易出现的、情理可容的异常状况。可查异常虽然是异常状况，
 * 但在一定程度上它的发生是可以预计的，而且一旦发生这种异常状况，就必须采取某种方式进行处理。
 * 除了RuntimeException及其子类以外，其他的Exception类及其子类都属于可查异常。这种异常的特点是Java编译器会检查它，
 * 也就是说，当程序中可能出现这类异常，要么用try-catch语句捕获它，要么用throws子句声明抛出它，否则编译不会通过。
 * 不可查异常(编译器不要求强制处置的异常):包括运行时异常（RuntimeException与其子类）和错误（Error）。
 * ————————————————
 * 原文链接：https://blog.csdn.net/huhui_cs/article/details/38817791
 *
 * @author Created by 思伟 on 2020/1/8
 */
public class SysRuntimeException extends BaseRuntimeException {
    private static final long serialVersionUID = 1L;

    protected int code = 500;
    protected String msg;

    public SysRuntimeException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public SysRuntimeException(int code, String msg, Exception cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public SysRuntimeException(String msg, Exception cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public SysRuntimeException(int code) {
        super("ERROR_CODE=" + code);
        this.code = code;
    }

    public SysRuntimeException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SysRuntimeException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
