package io.github.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Spring Context 工具类
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * Spring上下文
     * 使用@PostConstruct修饰的方法在Spring容器启动时会
     * 先于实现ApplicationContextAware接口的工具类 setApplicationContext()方法运行
     */
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    /**
     * 不抛出异常获取bean，如果有异常返回null
     */
    public static <T> T getBeanWithNoException(Class<T> requiredType) {
        if (null == applicationContext) {
            return null;
        }
        T bean = null;
        try {
            bean = applicationContext.getBean(requiredType);
        } catch (Exception e) {
        }
        return bean;
    }

    /**
     * 从单例集合获取Bean，不抛出异常获取bean，如果有异常返回null
     *
     * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#getBeanNamesForType(Class, boolean, boolean)
     */
    public static <T> T getBeanWithNoExceptionAndNoSingleton(Class<T> requiredType) {
        if (null == applicationContext) {
            return null;
        }
        T bean = null;
        Map<String, T> beans = null;
        try {
            beans = applicationContext.getBeansOfType(requiredType, false, true);
            Set<String> beanKeySet = beans.keySet();
            for (String beanKey : beanKeySet) {
                bean = beans.get(beanKey);
                // 默认获取第一个
                break;
            }
        } catch (Exception e) {
        }
        return bean;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getType(name);
    }

}