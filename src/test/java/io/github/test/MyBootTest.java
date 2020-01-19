package io.github.test;

import io.github.App;
import io.github.base.BaseAppTest;
import io.github.config.aop.MyController;
import io.github.util.ClassUtil;
import io.github.util.StringUtils;
import io.github.util.spring.SpringBeanUtils;
import io.github.util.spring.SpringContextUtils;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;

/**
 * 自定义测试类
 *
 * @author Created by 思伟 on 2020/1/17
 */
public class MyBootTest extends BaseAppTest {

    @Test
    public void testMyControllerRegistry() throws Exception {
        // 通过包名获取包内所有类
        List<Class<?>> classList = ClassUtil.getAllClassByPackageName(App.scanBasePackages);
        for (Class<?> clazz : classList) {
            // 类是否含有注解
            if (clazz.isAnnotationPresent(MyController.class)) {
                // 获取类路径
                String className = ClassUtil.getClass(clazz);
                try {
                    // 获取注解类
                    MyController myController = AnnotationUtils.findAnnotation(clazz, MyController.class);
                    // 声明beanName
                    String simpleName = null;
                    // 处理自定义beanName
                    simpleName = myController.value();
                    if (StringUtils.isEmpty(simpleName)) {
                        // 获取类名
                        simpleName = ClassUtil.getClassName(clazz);
                    }
                    simpleName = StringUtils.toLowerCaseFirstOne(simpleName);
                    // 这里因为还未注册Bean,需要先注册Bean（如果注册了跳过）
                    // 这里不能使用getBean方法判断，因为有缓存会导致后面不能注册
                    if (!SpringContextUtils.applicationContext.containsBeanDefinition(simpleName)) {
                        // 不存在Bean则手动注册Bean
                        SpringBeanUtils.addBean(className, simpleName, null);
                    }
                    // 注册Controller
                    SpringBeanUtils.registerController(clazz);
                    log.info("手动注册Controller[{}]成功,={}",
                            className, StringUtils.defaultString(myController.toString()));
                } catch (Exception e) {
                    log.error("手动注册Controller[{}]失败", className);
                    throw e;
                }
            } else {
                // log.info(ClassUtil.getClass(clazz));
            }
        }
    }

}
