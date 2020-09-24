package io.github.frame.prj.exception;

/**
 * 自定义异常接口
 *
 * @author Created by 思伟 on 2020/4/10
 */
public interface MyException {

    /**
     * 默认异常错误码
     */
    int DEFAULT_ERROR_CODE = 500;

    /**
     * 获取错误码
     *
     * @return {@link #DEFAULT_ERROR_CODE}
     * @since 1.8
     */
    default int getErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    /**
     * 获取错误信息
     *
     * @return String
     */
    String getMsg();

}
