package io.github.frame.spring.handler;

import io.github.frame.spring.IStartUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * 启动执行类，所有的启动执行调用，都应该写在这里
 *
 * @author Created by 思伟 on 2020/4/23
 */
@Slf4j
@Component
public class StartUpHandler implements ApplicationListener<ContextRefreshedEvent> {

    //    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // root application context 没有parent，他就是老大.
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() == null) {
            Map<String, IStartUp> startUpMap = applicationContext.getBeansOfType(IStartUp.class);

            Iterator<IStartUp> it = startUpMap.values().iterator();

            IStartUp startUpClazz;
            while (it.hasNext()) {
                startUpClazz = it.next();
                try {
                    startUpClazz.startUp(applicationContext);
                } catch (Exception e) {
                    log.error(String.format("启动%s失败，e=%s",
                            startUpClazz.getClass().getName(), e.getMessage()), e);
                }
            }
        }
    }
}
