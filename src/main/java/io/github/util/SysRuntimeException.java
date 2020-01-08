package io.github.util;

/**
 * 自定义异常类
 *
 * @author Created by 思伟 on 2020/1/8
 */
public class SysRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String code;
    protected String msg;

    public SysRuntimeException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public SysRuntimeException(String code, String msg, Exception cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public SysRuntimeException(String msg, Exception cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public SysRuntimeException(String code) {
        super("ERROR_CODE=" + code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
