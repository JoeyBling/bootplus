package io.github.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import io.github.util.freemaker.FormatTimeFtlHelper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * FreeMaker配置
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@org.springframework.context.annotation.Configuration
@Slf4j
public class FreeMarkerConfig {

    @Resource
    private Configuration configuration;
    @Resource
    protected org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver resolver;
    @Resource
    protected org.springframework.web.servlet.view.InternalResourceViewResolver springResolver;

    /**
     * 添加自定义标签
     *
     * @throws TemplateModelException
     */
    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
        configuration.setTagSyntax(freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX);
        configuration.setSharedVariable("formatTime", new FormatTimeFtlHelper());
        configuration.setSharedVariable("shiro", new ShiroTags());
    }

    @PostConstruct
    public void freeMarkerConfigurer() {
        log.debug("FreeMarkerConfig.freeMarkerConfigurer()...");
    }

}
