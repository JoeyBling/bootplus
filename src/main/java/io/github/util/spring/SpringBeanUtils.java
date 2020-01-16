package io.github.util.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Spring手动注册Bean
 *
 * @author Created by 思伟 on 2020/1/13
 */
@Slf4j
public class SpringBeanUtils {

    /**
     * Mapping映射对象
     */
    static final RequestMappingHandlerMapping requestMappingHandlerMapping =
            SpringContextUtils.getBean(RequestMappingHandlerMapping.class);

    /**
     * 去掉Controller的Mapping
     *
     * @param controllerBeanName
     */
    public static void unregisterController(String controllerBeanName) {
        if (requestMappingHandlerMapping != null) {
            String handler = controllerBeanName;
            Object controller = SpringContextUtils.getBean(handler);
            if (controller == null) {
                return;
            }
            final Class<?> targetClass = controller.getClass();
            ReflectionUtils.doWithMethods(targetClass, method -> {
                Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                try {
                    Method createMappingMethod = RequestMappingHandlerMapping.class.
                            getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
                    createMappingMethod.setAccessible(true);
                    RequestMappingInfo requestMappingInfo = (RequestMappingInfo)
                            createMappingMethod.invoke(requestMappingHandlerMapping, specificMethod, targetClass);
                    if (requestMappingInfo != null) {
                        requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
                    }
                } catch (Exception e) {
                    log.error(String.format("去掉Controller的Mapping失败,e=%s", e.getMessage()), e);
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
    }

    /**
     * 注册Controller
     *
     * @param controllerBeanName
     * @throws Exception
     */
    public static void registerController(String controllerBeanName) throws Exception {
        if (requestMappingHandlerMapping != null) {
            String handler = controllerBeanName;
            Object controller = SpringContextUtils.getBean(handler);
            if (controller == null) {
                return;
            }
            unregisterController(controllerBeanName);
            //注册Controller
            Method method = requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().
                    getDeclaredMethod("detectHandlerMethods", Object.class);
            method.setAccessible(true);
            method.invoke(requestMappingHandlerMapping, handler);
        }
    }

    /**
     * 注册Bean
     *
     * @param className   注册class全称
     * @param serviceName 注册别名
     * @param propertyMap 注入属性
     */
    private static void addBean(String className, String serviceName, Map<?, ?> propertyMap) {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            if (propertyMap != null) {
                Iterator<?> entries = propertyMap.entrySet().iterator();
                Map.Entry<?, ?> entry;
                while (entries.hasNext()) {
                    entry = (Map.Entry<?, ?>) entries.next();
                    String key = (String) entry.getKey();
                    Object val = entry.getValue();
                    beanDefinitionBuilder.addPropertyValue(key, val);
                }
            }
            registerBean(serviceName, beanDefinitionBuilder.getRawBeanDefinition());
        } catch (ClassNotFoundException e) {
            log.error(className + ",主动注册失败.");
        }
    }

    /**
     * 向spring容器注册bean
     *
     * @param beanName 注册别名
     */
    private static void registerBean(String beanName, BeanDefinition beanDefinition) {
        ConfigurableApplicationContext configurableApplicationContext =
                (ConfigurableApplicationContext) SpringContextUtils.applicationContext;
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext
                .getBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

}
