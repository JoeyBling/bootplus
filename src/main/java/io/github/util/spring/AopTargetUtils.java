package io.github.util.spring;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * Aop代理对象帮助类
 *
 * @author Created by 思伟 on 2020/1/16
 */
public class AopTargetUtils {

    /**
     * 获取目标对象
     *
     * @param proxy 代理对象
     * @throws Exception
     */
    public static <T> T getTargetSelf(T proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            // 不是代理对象
            return proxy;
        }

        // 是否是jdk动态接口代理
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return (T) getJdkDynamicProxyTargetObject(proxy);
        } else {
            // cglib类代理
            return (T) getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * 获取目标对象
     *
     * @param proxy 代理对象
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            // 不是代理对象
            return proxy;
        }

        // 是否是jdk动态接口代理
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else {
            // cglib类代理
            return getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * 获取cglib类代理目标对象
     *
     * @throws Exception
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).
                getTargetSource().getTarget();
        return target;
    }

    /**
     * 获取jdk动态接口代理目标对象
     *
     * @throws Exception
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

}
