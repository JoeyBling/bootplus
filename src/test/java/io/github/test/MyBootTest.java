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
                String className = ClassUtil.getClass(clazz);
                try {
                    // 获取注解类
                    MyController myController = AnnotationUtils.findAnnotation(clazz, MyController.class);

                    // 这里因为还未注册Bean,需要先注册Bean
                    if (null == SpringContextUtils.getBeanWithNoExceptionAndNoSingleton(clazz)) {
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
                    log.info("手动注册Controller[{}]成功,value={}",
                            className, StringUtils.defaultString(myController.value()));
                } catch (Exception e) {
                    log.info("手动注册Controller[{}]失败", className);
                    throw e;
                }
            } else {
                // log.info(ClassUtil.getClass(clazz));
            }
        }
    }

}
