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
     * 文件日志输出Logger名
     */
    private final String FILE_STATEMENT_LOGGER_NAME = "bootplus.file.Statement";

    private Map<String, Logger> loggerMap = new HashMap<String, Logger>(5);

    public Logger getFileStatementLogger() {
        Logger logger = getLoggerByMap(FILE_STATEMENT_LOGGER_NAME);
        if (null == logger) {
            logger = addLoggerForMap(FILE_STATEMENT_LOGGER_NAME);
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
     * @see #addLoggerForMap(String, Boolean)
     */
    private Logger addLoggerForMap(String loggerName) {
        return addLoggerForMap(loggerName, null);
    }

    /**
     * 获取日志打印对象并添加到缓存
     *
     * @param loggerName Logger名
     * @return Logger
     */
    private synchronized Logger addLoggerForMap(String loggerName, Boolean cover) {
        Logger logger = null;
        if (StringUtils.isNotEmpty(loggerName)) {
            logger = LoggerFactory.getLogger(FILE_STATEMENT_LOGGER_NAME);
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
