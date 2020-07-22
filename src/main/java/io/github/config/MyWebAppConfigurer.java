package io.github.config;

import com.google.common.collect.Lists;
import io.github.config.interceptor.LogInterceptor;
import io.github.frame.spring.web.servlet.MyHandlerInterceptor;
import io.github.util.file.FileUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 自定义配置
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Configuration
@Slf4j
public class MyWebAppConfigurer implements WebMvcConfigurer {

    private ApplicationProperties applicationProperties;

    /**
     * 需要提供的静态资源映射配置
     */
    protected List<ResourceHandlerInnerHelper> resourceHandlerInnerHelpers = Lists.newArrayList();

    /**
     * init serve
     */
    public MyWebAppConfigurer(@NonNull ApplicationProperties applicationProperties) {
        Assert.notNull(applicationProperties, "ApplicationProperties must not be null");
        this.applicationProperties = applicationProperties;
        // ResourceUtils.CLASSPATH_URL_PREFIX
        resourceHandlerInnerHelpers.add(new ResourceHandlerInnerHelper()
                .setPathPatterns(ArrayUtils.toArray("/statics/**"))
                .setResourceLocations(ArrayUtils.toArray("classpath:/statics/")));

        // For windows style path, we replace '\' to '/'.
        String uploadPath = FileUtils.generateFileUrl(applicationProperties.getFileConfig().getUploadPath());
        // 必须加上文件路径前缀
        if (!StringUtils.startsWithIgnoreCase(uploadPath, FileUtils.FILE_PREFIX)) {
            // 防止路径符号重复且映射路径必须以/结束
            uploadPath = FileUtils.generateFileUrl(FileUtils.FILE_PREFIX, uploadPath, File.separator);
        }
        // 媒体上传资源
        resourceHandlerInnerHelpers.add(new ResourceHandlerInnerHelper()
                .setPathPatterns(ArrayUtils.toArray(FILE_UPLOAD_PATH + "/**"))
                .setResourceLocations(ArrayUtils.toArray("classpath:/upload/", uploadPath)));

    }

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
        // 属性ignoreAcceptHeader默认为fasle，表示accept-header匹配，defaultContentType开启默认匹配
        // favorPathExtension表示支持后缀匹配
//        configurer.favorPathExtension(false);
    }

    /**
     * 自定义资源映射（如果存在2层映射资源路径有相同名字，后一个定义的优先）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 资源处理顺序（集合越前的解析顺序越高）
        int order = 1;
        for (ResourceHandlerInnerHelper resourceHandler :
                Optional.ofNullable(resourceHandlerInnerHelpers).orElse(Lists.newArrayList())) {
            // CachePeriod ?
            registry.setOrder(order).addResourceHandler(resourceHandler.getPathPatterns())
                    .addResourceLocations(resourceHandler.getResourceLocations());
            ++order;
            log.info("自定义资源映射成功。URL=[{}],Locations=[{}]",
                    ArrayUtils.toString(resourceHandler.getPathPatterns()),
                    ArrayUtils.toString(resourceHandler.getResourceLocations()));
        }
    }

    /**
     * 自定义添加配置拦截器
     *
     * @see MyHandlerInterceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器list
        List<MyHandlerInterceptor> interceptorList = Arrays.asList(new LogInterceptor());
        interceptorList.stream().forEach(myHandlerInterceptor -> {
            registry.addInterceptor(myHandlerInterceptor).addPathPatterns(myHandlerInterceptor.getPath())
                    .excludePathPatterns(myHandlerInterceptor.getExcludePath());
            if (log.isDebugEnabled()) {
                log.debug("自定义拦截器[{}]初始化完成...", myHandlerInterceptor.toString());
            }
        });
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
        factory.setMaxFileSize(DataSize.of(10, DataUnit.MEGABYTES));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(20, DataUnit.MEGABYTES));
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
        // 默认首页
//        registry.addViewController("/").setViewName("forward:/admin");
        Optional<ApplicationProperties> applicationProperties = Optional.ofNullable(this.applicationProperties);
        applicationProperties.ifPresent(properties -> {
            Optional.ofNullable(properties.getMvc()).ifPresent(mvc -> {
                Optional.ofNullable(mvc.getViewResolves()).ifPresent(viewResolves -> {
                    log.debug("自定义简单响应自动控制器={}", viewResolves.toString());
                    viewResolves.stream().forEach(viewResolve -> {
                        registry.addViewController(viewResolve.getUrlPath())
                                .setViewName(viewResolve.getViewName());
                    });
                });
            });
        });
    }

    /**
     * 外部访问用-上传文件映射访问路径
     */
    public static final String FILE_UPLOAD_PATH_EXT = "upload";

    /**
     * 上传文件映射访问路径（以/开头，结束无/）
     */
    public static final String FILE_UPLOAD_PATH = "/".concat(FILE_UPLOAD_PATH_EXT);

    /**
     * 自定义资源映射内部帮助类
     */
    @Data
    @Accessors(chain = true)
    protected static class ResourceHandlerInnerHelper {

        /**
         * URL路径
         *
         * @see ResourceHandlerRegistry#addResourceHandler(java.lang.String...)
         */
        private String[] pathPatterns;

        /**
         * 资源位置
         *
         * @see ResourceHandlerRegistration#addResourceLocations(java.lang.String...)
         */
        private String[] resourceLocations;
    }

}
