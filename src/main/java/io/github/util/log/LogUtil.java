package io.github.util.log;

import io.github.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具类
 * TODO 后期实现通用日志打印
 *
 * @author Created by 思伟 on 2020/3/17
 */
public class LogUtil {

    /**
     * 外部不提供实例化方法
     */
    private LogUtil() {
        super();
    }

    /**
     * 日志工具类
     */
    private static LogUtil logUtil;

    /**
     * 获取唯一实例对象
     */
    public static LogUtil getInstance() {
        if (null == logUtil) {
            logUtil = new LogUtil();
        }
        return logUtil;
    }

    /**
     * 统一文件日志输出Logger名
     */
    private final String FILE_STATEMENT_LOGGER_NAME = "bootplus.file.Statement";
    /**
     * 拦截器日志输出Logger名
     */
    private final String INTERCEPTOR_STATEMENT_LOGGER_NAME = "bootplus.interceptor.Statement";

    private Map<String, Logger> loggerMap = new HashMap<String, Logger>(5);

    /**
     * 拦截器日志
     * 最低级别-debug
     */
    public Logger getInterceptorStatementLogger() {
        return getLogger(INTERCEPTOR_STATEMENT_LOGGER_NAME);
    }

    /**
     * 统一文件日志
     * 最低级别-warn
     */
    public Logger getFileStatementLogger() {
        return getLogger(FILE_STATEMENT_LOGGER_NAME);
    }

    private Logger getLogger(String loggerName) {
        Logger logger = getLoggerByMap(loggerName);
        if (null == logger) {
            logger = addLoggerToMap(loggerName);
        }
        return logger;
    }

    /**
     * 从缓存拿日志打印对象
     *
     * @param loggerName Logger名
     * @return Logger
     */
    public Logger getLoggerByMap(String loggerName) {
        return null != loggerMap ? loggerMap.get(loggerName) : null;
    }

    /**
     * @see #addLoggerToMap(String, Boolean)
     */
    private Logger addLoggerToMap(String loggerName) {
        return addLoggerToMap(loggerName, null);
    }

    /**
     * 获取日志打印对象并添加到缓存
     *
     * @param loggerName Logger名
     * @return Logger
     */
    private synchronized Logger addLoggerToMap(String loggerName, Boolean cover) {
        Logger logger = null;
        if (StringUtils.isNotEmpty(loggerName)) {
            logger = LoggerFactory.getLogger(loggerName);
            if (null != loggerMap) {
                if (loggerMap.containsKey(loggerName)) {
                    // 判断是否已存在且覆盖
                    if (Boolean.TRUE.equals(cover)) {
                        loggerMap.put(loggerName, logger);
                    }
                } else {
                    loggerMap.put(loggerName, logger);
                }
            }
        }
        return logger;
    }

}
