package io.github.config;

import io.github.config.interceptor.LogInterceptor;
import io.github.util.http.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义配置
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Configuration
@Slf4j
@PropertySource({"classpath:/config.properties"})
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
    /**
     * 自定义资源映射（如果存在2层映射资源路径有相同名字，后一个定义的优先）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // For windows style path, we replace '\' to '/'.
        uploadPath = RestTemplateUtil.generateFileUrl(uploadPath);
        // 必须加上文件路径前缀
        if (!StringUtils.startsWithIgnoreCase(uploadPath, FILE_PREFIX)) {
            uploadPath = FILE_PREFIX.concat(uploadPath);
        }
        // 映射路径必须以/结束
        if (!StringUtils.endsWithAny(uploadPath, "/", "\\")) {
            uploadPath = uploadPath.concat(File.separator);
        }
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        // 媒体资源
        registry.setOrder(3).addResourceHandler(FILE_UPLOAD_PATH + "/**").addResourceLocations("classpath:/upload/", uploadPath);
        log.info("自定义资源映射成功---{}", this.toString());
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器-list转数组
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").
                excludePathPatterns(EXCLUDE_PATH.toArray(new String[EXCLUDE_PATH.size()]));
        if (log.isDebugEnabled()) {
            log.debug("自定义拦截器初始化完成...");
        }
        super.addInterceptors(registry);
    }

    /**
     * 文件上传配置
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // KB,MB设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
        factory.setMaxFileSize("10MB");
        // 设置总上传数据总大小
        factory.setMaxRequestSize("20MB");
        // Sets the directory location where files will be stored.
        // factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }

    /**
     * 拦截器排除路径
     */
    private final static List<String> EXCLUDE_PATH = Arrays.asList("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");

    /**
     * 文件路径前缀
     */
    public static final String FILE_PREFIX = "file:/";

    /**
     * 外部访问用-上传文件映射访问路径
     */
    public static final String FILE_UPLOAD_PATH_EXT = "upload";

    /**
     * 上传文件映射访问路径（以/开头，结束无/）
     */
    public static final String FILE_UPLOAD_PATH = "/".concat(FILE_UPLOAD_PATH_EXT);

    /**
     * 文件上传保存路径
     */
    @Value("${file.UploadPath:#{myWebAppConfigurer.FILE_PREFIX + myWebAppConfigurer.FILE_UPLOAD_PATH}}")
    public String uploadPath;

    @Override
    public String toString() {
        return "MyWebAppConfigurer{" +
                "uploadPath='" + uploadPath + '\'' +
                '}';
    }

}
