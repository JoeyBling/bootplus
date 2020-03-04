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
    public final static String CONTEXT_PATH_PROPERTY_NAME = "web.root.context.admin";

    /**
     * 上下文真实路径系统参数名称
     */
    public final static String CONTEXT_REAL_PATH_PROPERTY_NAME = "web.root.admin";

    /**
     * 应用程序上下文路径
     */
    public static String contextPath;

    /**
     * 应用程序上下文真实路径
     */
    public static String contextRealPath;

    /**
     * 创建时执行
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        contextPath = StringUtils.defaultString(servletContextEvent.getServletContext().getContextPath());
        contextRealPath = StringUtils.defaultString(servletContextEvent.getServletContext().getRealPath("/"));
        System.setProperty(CONTEXT_PATH_PROPERTY_NAME, contextPath);
        System.setProperty(CONTEXT_REAL_PATH_PROPERTY_NAME, contextRealPath);
        if (log.isDebugEnabled()) {
            log.debug("自定义监听器:ServletContextListener->contextInitialized=={}",
                    this.toString());
        }
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

    @Override
    public String toString() {
        return "ContextListener{" +
                "contextPath='" + contextPath + '\'' +
                ", contextRealPath='" + contextRealPath + '\'' +
                '}';
    }
}