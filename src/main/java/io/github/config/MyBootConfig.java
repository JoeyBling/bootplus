package io.github.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.config.aop.service.MyInjectBeanSelfProcessor;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 放一些自定义的Bean声明
 *
 * @author Created by 思伟 on 2019/12/16
 */
@Configuration
@EnableAsync
public class MyBootConfig {

    /**
     * 线程池维护线程的最少数量
     */
    @Value("${thread-pool.core-pool-size:2}")
    private int corePoolSize;

    /**
     * 线程池维护线程的最大数量
     */
    @Value("${thread-pool.max-pool-size:1000}")
    private int maxPoolSize;

    /**
     * 线程池所使用的缓冲队列
     */
    @Value("${thread-pool.queue-capacity:200}")
    private int queueCapacity;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    @Value("${thread-pool.keep-alive-seconds:2000}")
    private int keepAliveSeconds;

    /**
     * 配置线程池中的线程的名称前缀
     */
    @Value("${thread-pool.thread-name-prefix:async-resource-schedule-}")
    private String threadNamePrefix;

    /**
     * Spring线程池
     *
     * @return TaskExecutor
     */
    @Bean(name = AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    @ConditionalOnMissingBean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 手动注入self解决内部方法调用事务注解无效的问题
     */
    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new MyInjectBeanSelfProcessor();
    }

    /**
     * Jackson配置
     *
     * @return ObjectMapper
     * org.springframework.boot.autoconfigure.condition.OnBeanCondition#getMatchOutcome(ConditionContext, AnnotatedTypeMetadata)
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass({"io.gitee.zhousiwei.FastJsonAutoConfiguration"})
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        // 取消timestamps形式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        // 设置时区
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.err.println("starter for Jackson-----Jackson init success.");
        return mapper;
    }

}


