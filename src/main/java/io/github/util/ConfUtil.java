package io.github.util;

import io.github.frame.constant.SystemConst;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置工具类
 *
 * @author Created by 思伟 on 2019/12/18
 */
@Slf4j
@Deprecated
public class ConfUtil {

    /**
     * 内部配置类集合
     */
    private static Map<String, ConfUtil> configUtils = new HashMap<String, ConfUtil>();

    /**
     * 配置读取工具类
     */
    private Properties properties = new Properties();

    /**
     * 获取唯一实例
     *
     * @see #getInstance(String, String)
     */
    public static ConfUtil getInstance(String confName) {
        return getInstance(confName, SystemConst.DEFAULT_CHARSET.name());
    }

    /**
     * 获取唯一实例
     *
     * @param confName 配置文件名称(无需后缀)
     * @param charset  编码
     * @return ConfUtil
     */
    public static ConfUtil getInstance(String confName, String charset) {
        ConfUtil configUtil = configUtils.get(confName);
        if (configUtil == null) {
            configUtil = newConfigUtil(confName, charset);
        }
        return configUtil;
    }

    /**
     * 新建配置工具类
     *
     * @param confName 配置文件名称(无需后缀)
     * @param charset  编码
     * @return 配置工具类
     */
    private static ConfUtil newConfigUtil(String confName, String charset) {

        ConfUtil configUtil = new ConfUtil(confName, charset);
        configUtils.put(confName, configUtil);

        return configUtil;
    }

    /**
     * 内部构造方法
     */
    private ConfUtil(String confName, String charset) {
        init(confName, charset);
    }

    /**
     * 初始化配置
     *
     * @param confName 配置文件名称(无需后缀)
     * @param charset  编码
     */
    private void init(String confName, String charset) {
        try (InputStream fileInputStream = this.getClass().getResourceAsStream("/" + confName + ".properties")) {
            try (InputStreamReader reader = new InputStreamReader(fileInputStream, charset)) {
                properties.load(reader);
            }
        } catch (Throwable e) {
            log.error("配置文件加载错误[/" + confName + ".properties]", e);
        }
    }

    /**
     * 获取String
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取Integer
     */
    public Integer getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * 获取Long
     */
    public Long getLong(String key) {
        return Long.parseLong(properties.getProperty(key));
    }

    /**
     * 获取Boolean
     */
    public Boolean getBoolean(String key) {
        return "true".equals(getString(key)) || "0".equals(getString(key));
    }

}
