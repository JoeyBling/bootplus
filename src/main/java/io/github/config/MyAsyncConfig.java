package io.github.config;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 自定义异步配置
 *
 * @author Created by 思伟 on 2020/7/13
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class MyAsyncConfig implements AsyncConfigurer {

    /**
     * 自定义线程池，若不重写会使用默认的线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        return null;
//        return taskExecutor(null);
    }

    /**
     * 异常处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    /**
     * 处理异步方法引发的未捕获异常的策略
     * A default {@link AsyncUncaughtExceptionHandler} that simply logs the exception.
     * 被@Async 的方法在独立线程调用，不能被@ControllerAdvice全局异常处理器捕获，所以需要自己设置异常处理
     *
     * @see org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
     */
    @Slf4j
    /* non-public */ static class MyAsyncExceptionHandler extends SimpleAsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... objects) {
            if (log.isErrorEnabled()) {
                log.error(String.format("Unexpected error occurred invoking async " +
                        "method '%s'.", method), ex);
                if (ObjectUtil.isNotEmpty(objects)) {
                    for (Object param : objects) {
                        log.error("Parameter value - " + param);
                    }
                }
            }
        }
    }

}
