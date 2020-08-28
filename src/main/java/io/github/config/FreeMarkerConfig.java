package io.github.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModelException;
import io.github.frame.spring.IStartUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * FreeMaker配置
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Component
@Slf4j
public class FreeMarkerConfig implements IStartUp {

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
     * @see Configuration#setSetting(java.lang.String, java.lang.String)
     */
    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
//        configuration.setTagSyntax(freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX);
        configuration.setSharedVariable("shiro", new ShiroTags());
//        log.debug("FreeMarkerConfig添加自定义标签完成...");
    }

    /**
     * 添加自定义标签
     *
     * @throws TemplateModelException
     * @see MyTemplateDirectiveModel
     * @see Configuration#setSetting(java.lang.String, java.lang.String)
     */
    @Override
    public void startUp(ApplicationContext applicationContext) throws Exception {
        final Map<String, MyTemplateDirectiveModel> beans = applicationContext.getBeansOfType(MyTemplateDirectiveModel.class);
        Optional.ofNullable(beans).orElse(new HashMap<>(1)).forEach((beanName, ele) -> {
            configuration.setSharedVariable(ele.getTagName(), ele);
            log.debug("FreeMarker添加自定义标签[{}]完成...", ele.getTagName());
        });
    }

    /**
     * 自定义FreeMarker标签接口
     */
    public interface MyTemplateDirectiveModel extends TemplateDirectiveModel {

        /**
         * 获取访问标签的名称
         *
         * @return String
         * @see Configuration#setSharedVariable
         */
        String getTagName();

    }

}
