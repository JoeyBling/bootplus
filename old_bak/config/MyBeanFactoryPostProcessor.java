package io.github.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义BeanFactoryPostProcessor
 *
 * @author Created by 思伟 on 2020/7/22
 */
@Configuration
@Deprecated
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不建议修改
        ((AbstractAutowireCapableBeanFactory) beanFactory).setAllowRawInjectionDespiteWrapping(true);
    }

}
