package io.github.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 程序自定义配置
 *
 * @author Created by 思伟 on 2020/5/11
 * @NotEmpty 用在集合上面(不能注释枚举)
 * @NotBlank 用在String上面
 * @NotNull 用在所有类型上面
 */
@Data
@Validated
@Primary
@Component
@ConfigurationProperties(prefix = ApplicationProperties.APPLICATION_CONFIG_PREFIX)
@ConfigurationPropertiesBinding
public class ApplicationProperties {
    /**
     * 程序自定义配置前缀
     */
    public static final String APPLICATION_CONFIG_PREFIX = "application";

    /**
     * 管理员ID
     */
    @NotNull(message = "管理员ID不能为null")
    private Long adminId;

    /**
     * 程序名称
     */
    @NotBlank
    private String name;

    /**
     * 版本号
     */
    @NotBlank
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
     * 博客地址
     */
    @NotBlank
    private String blog;

    /**
     * @see MyLoggerConfig
     */
    @NestedConfigurationProperty
    private MyLoggerConfig logs = new MyLoggerConfig();

    /**
     * @see MyThreadPoolConfig
     */
    @NestedConfigurationProperty
    private MyThreadPoolConfig threadPool = new MyThreadPoolConfig();

    /**
     * @see Mvc
     */
    @NestedConfigurationProperty
    private Mvc mvc = new Mvc();

    /**
     * @see FileSystemConfig
     */
    @NestedConfigurationProperty
    private FileSystemConfig fileConfig = new FileSystemConfig();

    /**
     * @see JunitEnvConfig
     */
    @NestedConfigurationProperty
    private JunitEnvConfig junitEnv;

    /**
     * SpringMVC配置
     */
    @Data
    public static class Mvc {

        private List<ViewResolve> viewResolves;

        // TODO
        private List<Cors> cors = new ArrayList<Cors>(Arrays.asList(new Cors()));

        /**
         * 简单响应自动控制器
         */
        @Data
        public static class ViewResolve {

            /**
             * URL路径（或模式）
             * <p>Patterns like {@code "/admin/**"} or {@code "/articles/{articlename:\\w+}"}
             */
            private String urlPath;

            /**
             * 视图名称
             */
            private String viewName;

        }

        /**
         * CORS跨域配置
         */
        @Data
        public static class Cors {
            private List<String> allowedOrigins = Lists.newArrayList("*");
            private List<String> allowedHeaders = Lists.newArrayList("*");
            private List<String> allowedMethods = Lists.newArrayList("*");
            private Boolean allowCredentials = true;
            private Long maxAge = 3600L;
            private String path = "/**";
        }

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

    /**
     * 文件系统配置
     */
    @Data
    @Accessors(chain = true)
    public static class FileSystemConfig {

        /**
         * 文件上传保存文件夹路径日期格式
         */
        private String uploadPathDataFormat = "yyyyMM";

        /**
         * 文件上传保存路径
         */
        private String uploadPath;

        /**
         * 文件上传类型映射集合
         * k: 上传的文件类型数字key
         * v: 上传的文件类型名称
         */
        private Map<Integer, String> uploadTypeMapping = new ImmutableMap.Builder<Integer, String>()
                // 管理员上传头像
                .put(0, "adminAvatar")
                // 其他
                .put(-1, "other").build();

    }

    /**
     * Junit环境配置
     */
    @Data
    @Accessors(chain = true)
    public static class JunitEnvConfig {
        /**
         * 管理员账号
         */
        private String adminName;

        /**
         * 管理员账号密码
         */
        private String adminPwd;
    }

    //    @DeprecatedConfigurationProperty(reason = "暂时弃用")
    public String getUrl() {
        return url;
    }

}
