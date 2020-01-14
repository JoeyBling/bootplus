package io.github.util.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Spring手动注册Bean
 *
 * @author Created by 思伟 on 2020/1/13
 */
@Slf4j
public class SpringBeanUtils {

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
                    e.printStackTrace();
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


}
