package io.github.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import io.github.config.aop.service.MyInjectBeanSelfProcessor;
import io.github.util.DateUtils;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 放一些自定义的Bean声明
 *
 * @author Created by 思伟 on 2019/12/16
 */
@Configuration
public class MyBootConfig {

    /**
     * Spring线程池
     *
     * @return TaskExecutor
     */
    @Bean(name = AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    @ConditionalOnMissingBean
    public TaskExecutor taskExecutor(ApplicationProperties applicationProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(applicationProperties.getThreadPool().getCorePoolSize());
        executor.setMaxPoolSize(applicationProperties.getThreadPool().getMaxPoolSize());
        executor.setQueueCapacity(applicationProperties.getThreadPool().getQueueCapacity());
        executor.setKeepAliveSeconds(applicationProperties.getThreadPool().getKeepAliveSeconds());
        executor.setThreadNamePrefix(applicationProperties.getThreadPool().getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 手动注入self解决内部方法调用事务注解无效的问题
     * doc: @Bean导致`Ordered`接口失效
     */
    @Bean
    @ConditionalOnMissingBean
    @Deprecated
    public BeanPostProcessor beanPostProcessor() {
        return new MyInjectBeanSelfProcessor();
    }

    /**
     * 程序自定义配置
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    /**
     * Jackson配置
     *
     * @return ObjectMapper
     * org.springframework.boot.autoconfigure.condition.OnBeanCondition#getMatchOutcome(ConditionContext, AnnotatedTypeMetadata)
     * @see JacksonAutoConfiguration
     */
//    @Bean
    @Deprecated
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass({"io.gitee.zhousiwei.FastJsonProperties"})
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        // 序列化的规则
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 取消timestamps形式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN);
        mapper.setDateFormat(dateFormat);
        //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        // 设置时区
        mapper.setTimeZone(TimeZone.getTimeZone(DateUtils.DATE_TIMEZONE));
        System.err.println("starter for Jackson-----Jackson init success.");
        return mapper;
    }

    /**
     * 验证码生成器
     */
    @Bean
    @ConditionalOnMissingBean
    public Producer kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.put("kaptcha.border", "no");
        // 字体颜色
        properties.put("kaptcha.textproducer.font.color", "black");
        // 文字间隔
        properties.put("kaptcha.textproducer.char.space", "4");
        // 验证码文本字符长度默认为5
        properties.put("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

    /**
     * 自定义ErrorAttributes
     *
     * @see org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration#errorAttributes()
     */
    @Component
    public class MyErrorAttributes extends DefaultErrorAttributes {

        private ApplicationProperties appConfig;

        public MyErrorAttributes(ApplicationProperties applicationProperties) {
            Assert.notNull(applicationProperties, "ApplicationProperties must not be null");
            this.appConfig = applicationProperties;
        }

        @Override
        public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
            final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
//            errorAttributes.put("version", appConfig.getVersion());
            errorAttributes.put("author", "試毅-思伟");
            errorAttributes.put("blog", appConfig.getBlog());
            return errorAttributes;
        }

    }

}


