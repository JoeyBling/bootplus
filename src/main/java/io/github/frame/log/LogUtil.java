package io.github.frame.log;

import io.github.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工具类
 * 对于 `trace/debug/info` 级别的日志输出，必须进行日志级别的开关判断
 *
 * @author Created by 思伟 on 2020/3/17
 */
public class LogUtil {

    /**
     * 获取唯一实例对象
     */
    public static LogUtil getInstance() {
        if (null == logUtil) {
            synchronized (LogUtil.class) {
                if (null == logUtil) {
                    logUtil = new LogUtil();
                }
            }
        }
        return logUtil;
    }

    /**
     * 拦截器日志
     * 最低级别-debug
     */
    public MyLogger getInterceptorStatementLogger() {
        return getLogger(INTERCEPTOR_STATEMENT_LOGGER_NAME);
    }

    /**
     * 统一文件日志
     * 最低级别-warn
     */
    public MyLogger getFileStatementLogger() {
        return getLogger(FILE_STATEMENT_LOGGER_NAME);
    }

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
     * 统一文件日志输出Logger名
     */
    private final String FILE_STATEMENT_LOGGER_NAME = "bootplus.file.Statement";
    /**
     * 拦截器日志输出Logger名
     */
    private final String INTERCEPTOR_STATEMENT_LOGGER_NAME = "bootplus.interceptor.Statement";

    private Map<String, MyLogger> myLoggerMap = new ConcurrentHashMap<String, MyLogger>(16);

    /**
     * 获取日志打印对象
     *
     * @param loggerName Logger名
     * @return MyLogger
     */
    private MyLogger getLogger(String loggerName) {
        MyLogger logger = getLoggerByMap(loggerName);
        if (null == logger) {
            logger = addLoggerToMap(loggerName);
        }
        return logger;
    }

    /**
     * 从缓存拿日志打印对象
     *
     * @param loggerName Logger名
     * @return MyLogger
     */
    public MyLogger getLoggerByMap(String loggerName) {
        return null != myLoggerMap ? myLoggerMap.get(loggerName) : null;
    }

    /**
     * @see #addLoggerToMap(String, Boolean)
     */
    private MyLogger addLoggerToMap(String loggerName) {
        return addLoggerToMap(loggerName, null);
    }

    /**
     * 获取日志打印对象并添加到缓存
     *
     * @param loggerName Logger名
     * @param cover      判断是否已存在且覆盖
     * @return MyLogger
     */
    private synchronized MyLogger addLoggerToMap(String loggerName, Boolean cover) {
        MyLogger myLogger = null;
        if (StringUtils.isNotEmpty(loggerName)) {
            Logger logger = LoggerFactory.getLogger(loggerName);
            if (null != logger && null != myLoggerMap) {
                myLogger = new MyLogger(logger);
                if (myLoggerMap.containsKey(loggerName)) {
                    // 判断是否已存在且覆盖
                    if (Boolean.TRUE.equals(cover)) {
                        myLoggerMap.put(loggerName, myLogger);
                    }
                } else {
                    myLoggerMap.put(loggerName, myLogger);
                }
            }
        }
        return myLogger;
    }

    /**
     * 重写日志打印
     *
     * @see Logger
     */
    public class MyLogger {

        public MyLogger(Logger logger) {
            Assert.notNull(logger, "logger must not be null");
            this.logger = logger;
        }

        /**
         * @see Logger#debug(String)
         */
        public void debug(String msg) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(msg);
            }
        }

        /**
         * @see Logger#debug(String, Object...)
         */
        public void debug(String format, Object... arguments) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(format, arguments);
            }
        }

        /**
         * @see Logger#debug(String, Throwable)
         */
        public void debug(String msg, Throwable t) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug(msg, t);
            }
        }

        /**
         * @see Logger#info(String)
         */
        public void info(String msg) {
            if (getLogger().isInfoEnabled()) {
                getLogger().info(msg);
            }
        }

        /**
         * @see Logger#info(String, Object...)
         */
        public void info(String format, Object... arguments) {
            if (getLogger().isInfoEnabled()) {
                getLogger().info(format, arguments);
            }

        }

        /**
         * @see Logger#info(String, Throwable)
         */
        public void info(String msg, Throwable t) {
            if (getLogger().isInfoEnabled()) {
                getLogger().info(msg, t);
            }
        }

        /**
         * @see Logger#error(String)
         */
        public void error(String msg) {
            if (getLogger().isErrorEnabled()) {
                getLogger().error(msg);
            }
        }

        /**
         * 获取日志对象
         *
         * @return Logger
         */
        public Logger getLogger() {
            return logger;
        }

        /**
         * 日志对象
         */
        private Logger logger;

    }

}
