package io.github.common.hessian;

import javax.servlet.ServletRequest;

/**
 * Hessian上下文请求工具类
 *
 * @author Created by 思伟 on 2020/7/10
 */
public class HessianContext {

    /**
     * 外部不提供实例化方法
     */
    private HessianContext() {
    }

    /**
     * request请求对象
     */
    private ServletRequest servletRequest;

    private static final ThreadLocal<HessianContext> LOCAL_CONTEXT =
            ThreadLocal.withInitial(HessianContext::new);

    /**
     * 设置request请求对象
     *
     * @param request ServletRequest
     */
    public static void setRequest(ServletRequest request) {
        LOCAL_CONTEXT.get().servletRequest = request;
    }

    /**
     * 获取request请求对象
     *
     * @return ServletRequest
     */
    public static ServletRequest getRequest() {
        return LOCAL_CONTEXT.get().servletRequest;
    }

    /**
     * 清除本地线程变量
     */
    public static void clear() {
        // 回收自定义的ThreadLocal变量
        LOCAL_CONTEXT.remove();
    }

}
