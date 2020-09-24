package io.github.frame.spring.context.handler;

import io.github.frame.prj.service.annotation.ApiMethod;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.frame.prj.transfer.request.TransBaseRequest;
import io.github.frame.prj.service.ApiService;
import io.github.frame.spring.IStartUp;
import io.github.util.ClassUtil;
import io.github.util.MapUtil;
import io.github.frame.prj.exception.SysRuntimeException;
import io.github.util.spring.AopTargetUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口扫描执行类
 *
 * @author Created by 思伟 on 2020/8/3
 * @see ApiService
 */
@Slf4j
@Component
public class ApiServiceInitService implements IStartUp {
    /**
     * Api接口集合
     */
    public static final Map<String, ApiMethodHandler> API_METHOD_MAP =
            new ConcurrentHashMap<String, ApiMethodHandler>(MapUtil.DEFAULT_INITIAL_CAPACITY);

    @Override
    public void startUp(final ApplicationContext applicationContext) throws Exception {
        // 只对实现`ApiService`的Bean进行扫描
        Map<String, ApiService> beans = applicationContext.getBeansOfType(ApiService.class);
        Optional.ofNullable(beans).orElse(new HashMap<>(1)).entrySet().forEach(apiServiceEntry -> {
            final String beanName = apiServiceEntry.getKey();
            final ApiService apiService = apiServiceEntry.getValue();
            if (null != apiService) {
                ApiService targetSelf = null;
                if (AopUtils.isAopProxy(apiService)) {
                    try {
                        targetSelf = AopTargetUtils.getTargetSelf(apiService);
                    } catch (Exception e) {
                        log.error("获取目标代理对象错误,{}...", e.getMessage());
                    }
                }
                targetSelf = null == targetSelf ? apiService : targetSelf;
                // 此处不能使用代理对象进行反射处理，不然会被执行2次(代理对象一次，目标对象一次)
                ReflectionUtils.doWithMethods(targetSelf.getClass(), method -> {
                    ApiMethod serviceMethod = AnnotationUtils.findAnnotation(method, ApiMethod.class);
                    if (serviceMethod != null) {
                        ApiMethodHandler handler = new ApiMethodHandler()
                                .setHandlerMethod(method)
                                .setPermission(Optional.ofNullable(serviceMethod.permission())
                                        // 接口类默认的鉴权模式
                                        .orElse(apiService.getPermission()));
                        // 如果当前对象是AOP代理对象，直接注入
                        if (AopUtils.isAopProxy(apiService)) {
                            handler.setHandler(apiService);
                        } else {
                            log.warn("The beanName[{}] is not a Aop target,please fixed it...",
                                    beanName);
                            handler.setHandler(applicationContext.getBean(beanName));
                        }
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        handler.setRequestTypes(parameterTypes);

                        // 方法名
                        final String methodName = method.getName();
                        // 校验入参定义
                        for (Class<?> cls : parameterTypes) {
                            if (!TransBaseRequest.class.isAssignableFrom(cls)
                                    && !HttpServletRequest.class.equals(cls)
                                    && !HttpServletResponse.class.equals(cls)) {
                                throw new SysRuntimeException(ResponseCodeConst.ERROR_VALIDATE,
                                        method.getDeclaringClass().getName() + "." + methodName + "的允许入参错误");
                            }
                        }
                        if (null == serviceMethod.name() || serviceMethod.name().length < 1) {
                            log.warn("[{}]定义的接口名为空，使用默认方法名[{}]",
                                    ClassUtil.getClass(method.getDeclaringClass()), methodName);
                            registerApiMethod(methodName, handler);
                        } else {
                            for (String serviceName : serviceMethod.name()) {
                                registerApiMethod(serviceName, handler);
                            }
                        }
                    }
                }, method -> !method.isSynthetic() && AnnotationUtils.findAnnotation(method, ApiMethod.class) != null);
            }
        });

    }

    /**
     * 注册API接口类
     *
     * @param methodName 接口名
     * @param handler    Api接口处理对象
     */
    public void registerApiMethod(String methodName, ApiMethodHandler handler) {
        synchronized (API_METHOD_MAP) {
            if (API_METHOD_MAP.containsKey(methodName)) {
                log.error("重复的API[{}]", methodName);
            } else {
                API_METHOD_MAP.put(methodName, handler);
                log.info("注册API键值：{}", methodName);
            }
        }
    }

    /**
     * Api接口处理
     *
     * @author Created by 思伟 on 2020/8/3
     */
    @Data
    @Accessors(chain = true)
    public class ApiMethodHandler {

        /**
         * 处理器对象
         */
        private Object handler;

        /**
         * 处理器的处理方法
         */
        private Method handlerMethod;
        /**
         * 处理方法的请求对象类
         */
        private Class<?>[] requestTypes = null;

        /**
         * 接口调用权限(可设置多个)
         */
        private String[] permission;

    }

}
