package io.github.config.aop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * bean自调用切面(解决Spring-AOP嵌套调用)
 * 只要是在Shiro-AuthorizingRealm接口注入的Bean声明,所有相关联的Bean都被初始化完成且没有被代理[代理会在SpringBoot中无效]（包括BeanPostProcessor也会无效）
 * 手动注入self解决内部方法调用事务注解无效的问题
 * self会在编译的时候和this具有相同的效果，而this是关键字
 *
 * @author Created by 思伟 on 2020/1/13
 */
@Slf4j
@Component
public class MyInjectBeanSelfProcessor implements BeanPostProcessor, ApplicationContextAware, Ordered {

    private ApplicationContext context;

    /**
     * ① 注入ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * 实例化、依赖注入、初始化完毕时执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // ② 如果Bean没有实现BeanSelfAware标识接口
        if (bean instanceof BeanSelfAware) {
            // ③ 如果当前对象是AOP代理对象，直接注入
            if (AopUtils.isAopProxy(bean)) {
                ((BeanSelfAware) bean).setSelf(bean);
            } else {
                // log.warn("The beanName[{}] is not a Aop target,please fixed it...", beanName);
                // ④ 如果当前对象不是AOP代理，则通过context.getBean(beanName)获取代理对象并注入
                // 此种方式不适合解决 prototype Bean的代理对象注入
                ((BeanSelfAware) bean).setSelf(context.getBean(beanName));
            }
            if (log.isDebugEnabled()) {
                log.debug("BeanName={},注入代理对象成功！", beanName);
            }
        }
        return bean;
    }

    /**
     * 实例化、依赖注入完毕，在调用显示的初始化之前完成一些定制的初始化任务
     * (@PostConstruct注解指定初始化方法，Java类实现InitializingBean接口)之前调用
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
//        return Ordered.LOWEST_PRECEDENCE;
    }

}
