package io.github.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * 程序自定义配置
 *
 * @author Created by 思伟 on 2020/5/11
 */
@Data
@Component
@ConfigurationProperties(prefix = ApplicationProperties.APPLICATION_CONFIG_PREFIX)
@ConfigurationPropertiesBinding
public class ApplicationProperties {
    /**
     * 程序自定义配置前缀
     */
    public static final String APPLICATION_CONFIG_PREFIX = "application";

    /**
     * 程序名称
     */
    @NotEmpty
    private String name;

    /**
     * 版本号
     */
    @NotEmpty
    private String version;

    /**
     * 主页
     */
    private String url;

    /**
     * 描述
     */
    private String description;

    /**
     * @see MyLoggerConfig
     */
    private MyLoggerConfig logs;

    /**
     * @see MyThreadPoolConfig
     */
    private MyThreadPoolConfig threadPool = new MyThreadPoolConfig();

    @DeprecatedConfigurationProperty(reason = "暂时弃用")
    public String getUrl() {
        return url;
    }

    /**
     * 自定义日志配置
     */
    @Data
    public static class MyLoggerConfig {
        /**
         * 日志等级
         */
        private String level;

        /**
         * 路径
         */
        private String path;
    }

    /**
     * 自定义线程池配置
     */
    @Data
    public static class MyThreadPoolConfig {
        /**
         * 线程池维护线程的最少数量
         */
        private int corePoolSize = 2;

        /**
         * 线程池维护线程的最大数量
         */
        private int maxPoolSize = 1000;

        /**
         * 线程池所使用的缓冲队列
         */
        private int queueCapacity = 200;

        /**
         * 线程池维护线程所允许的空闲时间
         */
        private int keepAliveSeconds = 2000;

        /**
         * 配置线程池中的线程的名称前缀
         */
        private String threadNamePrefix = "async-resource-schedule-";
    }

}
