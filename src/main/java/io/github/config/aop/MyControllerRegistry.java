package io.github.config.aop;

import io.github.App;
import io.github.util.ClassUtil;
import io.github.util.StringUtils;
import io.github.util.spring.SpringBeanUtils;
import io.github.util.spring.SpringContextUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Spring动态代理手动注册Controller
 * 关于@Controller和@Component注解只是个见名知意的意思,方便区分具体bean的作用而已
 * 实际是去判断类注解是否有@Controller.class | @RequestMapping.class 这2个才注册URL
 *
 * @author Created by 思伟 on 2020/1/16
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#isHandler(Class)
 * @see AbstractHandlerMethodMapping#initHandlerMethods()
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
                // 获取bean对象
                Object aopObject = entry.getValue();
                Class<? extends Object> clazz = aopObject.getClass();
                // 注册Controller
                SpringBeanUtils.registerController(clazz);
                // 获取注解类
                MyController myController = AnnotationUtils.findAnnotation(clazz, MyController.class);
                log.info("Spring手动注册Controller成功=={},value={}",
                        ClassUtil.getClass(aopObject), myController.value());
            }
//            handleRegister(App.scanBasePackages, annotationClass);
        }
    }

    /**
     * 貌似onApplicationEvent方法里注册Bean不生效
     * 使用@PostConstruct修饰的方法在Spring容器启动时会
     * 先于实现ApplicationContextAware接口的工具类 setApplicationContext()方法运行
     * 使用@DependsOn表示强制初始化该工具类【@DependsOn压根没用-~-】
     *
     * @throws Exception
     */
    @PostConstruct
    public void doSomeThing() throws Exception {
//        handleRegister(App.scanBasePackages, MyController.class);
    }

    /**
     * 通过扫描包名和注解类注册Controller
     *
     * @param scanBasePackages 扫描包名
     * @param annotationClass  注解类
     * @param <A>              Annotation
     * @throws Exception
     */
    private <A extends Annotation> void handleRegister(String scanBasePackages, Class<A> annotationClass) throws Exception {
        // 通过包名获取包内所有类
        List<Class<?>> classList = ClassUtil.getAllClassByPackageName(scanBasePackages);
        for (Class<?> clazz : classList) {
            // 类是否含有注解
            if (clazz.isAnnotationPresent(annotationClass)) {
                String className = ClassUtil.getClass(clazz);
                try {
                    // 获取注解类
                    A annotation = AnnotationUtils.findAnnotation(clazz, annotationClass);
                    // 这里因为还未注册Bean,需要先注册Bean（如果注册了跳过）
                    if (null == SpringContextUtils.getBeanWithNoException(clazz)) {
                        // 首字母转小写
                        String simpleName = StringUtils.toLowerCaseFirstOne(
                                ClassUtil.getClassName(clazz));
                        SpringBeanUtils.addBean(className, simpleName, null);
                    }
                    // 注册Controller
                    /**
                     * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#getBeanNamesForType(Class, boolean, boolean)
                     */
                    SpringBeanUtils.registerController(clazz);
                    log.info("手动注册Controller[{}]成功,={}",
                            className, StringUtils.defaultString(annotation.toString()));
                } catch (Exception e) {
                    log.info(String.format("手动注册Controller[%s]失败", className), e);
                    throw e;
                }
            } else {
                // log.info(ClassUtil.getClass(clazz));
            }
        }
    }

}
