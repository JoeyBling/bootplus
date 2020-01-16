package io.github.config.aop;

import io.github.util.ClassUtil;
import lombok.SneakyThrows;
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
        Class<? extends Annotation> annotationClass = MyController.class;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
        Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
        for (Map.Entry<String, Object> entry : entitySet) {
            //获取bean对象
            Object aopObject = entry.getValue();
            Class<? extends Object> clazz = aopObject.getClass();
            System.out.println("================" + clazz.getName());
            System.out.println("================" + ClassUtil.getClass(aopObject));
            MyController myController = AnnotationUtils.findAnnotation(clazz, MyController.class);
            System.out.println("================" + myController.value());

        }
    }

}
