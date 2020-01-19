package io.github.config.aop.service;

/**
 * @author Created by 思伟 on 2020/1/13
 * @see MyInjectBeanSelfProcessor#postProcessAfterInitialization(Object, String)
 */
public interface BeanSelfAware {

    /**
     * 注入自身代理对象
     *
     * @param proxyBean 代理对象
     */
    void setSelf(Object proxyBean);

}
