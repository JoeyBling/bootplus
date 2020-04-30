package io.github.config;

import io.github.config.interceptor.LogInterceptor;
import io.github.util.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义配置
 * // 2.0 WebMvcConfigurationSupport
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Configuration
@Slf4j
@PropertySource({"classpath:/config.properties"})
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    /**
     * 解决String乱码问题
     *
     * @see org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor#writeWithMessageConverters(Object, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse)
     */
    @Bean
    public HttpMessageConverter<String> defaultStringHttpMessageConverter() {
        /**
         * @see AbstractHttpMessageConverter#getDefaultCharset()
         */
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return converter;
    }

    /**
     * 添加StringHttpMessageConverter默认配置
     *
     * @see org.springframework.http.converter.AbstractHttpMessageConverter#addDefaultHeaders(HttpHeaders, Object, MediaType)
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        // 不建议使用添加，而是进行修改
//        converters.add(defaultStringHttpMessageConverter());
    }

    /**
     * 修改StringHttpMessageConverter默认配置
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.extendMessageConverters(converters);
        for (int i = 0; null != converters && i < converters.size(); i++) {
            HttpMessageConverter<?> httpMessageConverter = converters.get(i);
            if (httpMessageConverter.getClass().equals(StringHttpMessageConverter.class)) {
                converters.set(i, defaultStringHttpMessageConverter());
            }
        }
    }

    /**
     * https://www.iteye.com/blog/412887952-qq-com-2315133
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        super.configureContentNegotiation(configurer);
        // 属性ignoreAcceptHeader默认为fasle，表示accept-header匹配，defaultContentType开启默认匹配
        // favorPathExtension表示支持后缀匹配
//        configurer.favorPathExtension(false);
    }

    /**
     * 自定义资源映射（如果存在2层映射资源路径有相同名字，后一个定义的优先）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // For windows style path, we replace '\' to '/'.
        uploadPath = FileUtils.generateFileUrl(uploadPath);
        // 必须加上文件路径前缀
        if (!StringUtils.startsWithIgnoreCase(uploadPath, FILE_PREFIX)) {
            // 防止路径符号重复且映射路径必须以/结束
            uploadPath = FileUtils.generateFileUrl(FILE_PREFIX, uploadPath, File.separator);
        }
        // ResourceUtils.CLASSPATH_URL_PREFIX
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
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**")
                // 【强制】使用集合转数组的方法，必须使用集合的 toArray(T[] array)，传入的是类型完全一
                //致、长度为 0 的空数组。 等于 0，动态创建与 size 相同的数组，性能最好。
                .excludePathPatterns(EXCLUDE_PATH.toArray(new String[0]));
        if (log.isDebugEnabled()) {
            log.debug("自定义拦截器初始化完成...");
        }
        super.addInterceptors(registry);
    }

    /**
     * 文件上传配置
     * TODO org.springframework.web.multipart.MultipartException:
     * Could not parse multipart servlet request; nested exception is java.io.IOException:
     * The temporary upload location [/tmp/tomcat.*./work/Tomcat/localhost/ROOT] is not valid
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
        // 临时目录属性
        // Sets the directory location where files will be stored.
        // springboot打jar包通过java -jar启动的项目
        // 如果上传文件会在linux的/temp/下生成一个tomcat*的文件夹，
        // 上传的文件先要转换成临时文件保存在这个文件夹下面。
        // 由于临时/tmp目录下的文件，在长时间（10天）没有使用的情况下，
        // 就会被系统机制自动删除掉。所以如果系统长时间无人问津的话，就可能导致上面这个问题
        //————————————————
        // 配置项：server.tomcat.basedir=/home/temp
        // factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        // 默认首页
//        registry.addViewController("/").setViewName("forward:/admin");
        registry.addViewController("/").setViewName("redirect:/admin");
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
