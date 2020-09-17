package io.github.config.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 继承的类都可以取得AOP self对象属性【继承此类来解决内部方法调用事务失效问题及没有代理等问题】
 * （泛型：S为当前对象）
 *
 * @author Created by 思伟 on 2020/3/1
 * @see MyInjectBeanSelfProcessor#postProcessAfterInitialization(Object, String)
 */
public abstract class BaseAopContext<S> implements BeanSelfAware {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * self会在编译的时候和this具有相同的效果，而this是关键字
     * 使用内部方法时可以指定self... 或者不加关键字即可使用代理对象(勿使用this不然无效)
     * 不加关键字测试了好像不行，建议使用self
     */
    protected S self;

    @SuppressWarnings("unchecked")
    @Override
    public void setSelf(Object proxyBean) {
        if (null != proxyBean) {
            this.self = (S) proxyBean;
        }
    }

}
