package io.github.config.aop;

import io.github.util.ClassUtil;
import io.github.util.spring.SpringBeanUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * Spring动态代理手动注册Controller
 *
 * @author Created by 思伟 on 2020/1/16
 */
@Slf4j
@Component
public class MyControllerRegistry implements ApplicationListener<ContextRefreshedEvent>,
        ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // root application context 没有parent，他就是老大.
        if (event.getApplicationContext().getParent() == null) {
            // 这里只能获取注册了Bean的注解类
            Class<? extends Annotation> annotationClass = MyController.class;
            Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
            Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
            for (Map.Entry<String, Object> entry : entitySet) {
                //获取bean对象
                Object aopObject = entry.getValue();
                Class<? extends Object> clazz = aopObject.getClass();
                // 注册Controller
                SpringBeanUtils.registerController(clazz);
//                SpringBeanUtils.registerController(clazz);
                MyController myController = AnnotationUtils.findAnnotation(clazz, MyController.class);
                log.debug("MyController-Value====={}", myController.value());
                log.info("Spring手动注册Controller成功=={}", ClassUtil.getClass(aopObject));
            }
        }
    }

}
