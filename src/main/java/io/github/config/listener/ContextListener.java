package io.github.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
     * 上下文路径系统参数名称
     */
    private final static String contextPathPropertyName = "web.root.admin";
    /**
     * 应用程序上下文路径
     */
    private static String contextPath;

    /**
     * 创建时执行
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (log.isDebugEnabled()) {
            log.debug("自定义监听器:ServletContextListener->contextInitialized=={}",
                    servletContextEvent.getServletContext().getContextPath());
        }
        contextPath = StringUtils.defaultString(servletContextEvent.getServletContext().getContextPath());
        System.setProperty(contextPathPropertyName, contextPath);
    }

    /**
     * 销毁时执行
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (log.isDebugEnabled()) {
            log.debug("ServletContext对象销毁");
        }
    }

}