package io.github.config.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 监听ServletContext对象的创建以及销毁
 *
 * @author Created by 思伟 on 2020/1/10
 */
@Slf4j
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * 创建时执行
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.debug("自定义监听器:ServletContextListener->contextInitialized=={}",
                servletContextEvent.getServletContext().getContextPath());
    }

    /**
     * 销毁时执行
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.debug("ServletContext对象销毁");
    }

}