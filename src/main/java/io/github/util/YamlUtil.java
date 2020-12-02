package io.github.util;

import io.github.util.spring.SpringReplaceCallBack;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Yaml读取工具类(支持`${SpEL}`表达式)
 *
 * @author Created by 思伟 on 2020/5/19
 */
@Slf4j
public class YamlUtil {
    /**
     * 默认yml文件名称
     */
    private static final String DEFAULT_YAML = "application.yml";

    /**
     * 当前激活的yml文件名称
     */
    private static String activeYaml = null;

    /**
     * Yaml配置缓存集合
     */
    private static Map<String, HashMap<?, ?>> yamlPropertiesCacheMap = new HashMap<>();

    /**
     * 获取对应key的值
     *
     * @param propertyKey 参数key
     * @return String
     */
    public static String getProperty(final String propertyKey) {
        return Optional.ofNullable(getProperty(getActiveYaml(), propertyKey))
                .orElseGet(() -> {
                    if (DEFAULT_YAML.equals(activeYaml)) {
                        return "";
                    }
                    // log.debug("`activeYaml`[{}]不存在指定的参数key[{}],使用默认主Yaml文件...", activeYaml, propertyKey);
                    return getProperty(DEFAULT_YAML, propertyKey);
                });

    }

    /**
     * 从yaml获取对应key的值
     *
     * @param yamlFile    yaml文件名
     * @param propertyKey 参数key
     * @return String
     */
    private static String getProperty(final String yamlFile, final String propertyKey) {
        final HashMap map = loadYamlPropertiesByCache(yamlFile);
        String yamlValue = getYamlValue(map, propertyKey);
        if (StringUtils.isNotEmpty(yamlValue)) {
            yamlValue = RegexUtil.replaceAll(yamlValue, new SpringReplaceCallBack(false, map));
        }
        return yamlValue;
    }

    /**
     * 获取当前启用的yaml文件名
     */
    private static String getActiveYaml() {
        if (StringUtils.isBlank(activeYaml)) {
            // 走系统参数，在启动的时候需要使用`-Dspring.profiles.active=xx`的方式来配置
            String active = System.getProperty("spring.profiles.active");
            active = null != active ? active : getYamlValue(loadYamlPropertiesByCache(DEFAULT_YAML), "spring.profiles.active");
            activeYaml = (null == active || "".equals(active)) ? DEFAULT_YAML : ("application-" + active + ".yml");
        }
        return activeYaml;
    }

    /**
     * 从缓存获取Yaml配置
     *
     * @see #loadYamlProperties(String)
     */
    private static HashMap loadYamlPropertiesByCache(String yamlFile) {
        HashMap<?, ?> yamlProperties = getYamlPropertiesCacheMap().get(yamlFile);
        if (null == yamlProperties) {
            yamlProperties = loadYamlProperties(yamlFile);
            getYamlPropertiesCacheMap().put(yamlFile, yamlProperties);
        }
        return yamlProperties;
    }

    /**
     * FIXME 用`main`方法测试时，静态变量总是不初始化
     * 目前会导致初始化1次后，又重新初始化一次，导致数据丢失了
     * 待找出问题
     *
     * @return 缓存对象
     */
    public static Map<String, HashMap<?, ?>> getYamlPropertiesCacheMap() {
        if (null == yamlPropertiesCacheMap) {
            synchronized (YamlUtil.class) {
                if (null == yamlPropertiesCacheMap) {
                    yamlPropertiesCacheMap = new HashMap<>();
                    // TODO 待删除
                    yamlPropertiesCacheMap.put("test", null);
                }
            }
        }
        return yamlPropertiesCacheMap;
    }

    /**
     * 将yaml文件加载为HashMap
     */
    private static HashMap<?, ?> loadYamlProperties(String yamlFile) {
        try (InputStream is = YamlUtil.class.getClassLoader().getResourceAsStream(yamlFile)) {
            // 防止文件不存在读取错误
            return Optional.ofNullable(is).map(inputStream -> new Yaml().loadAs(is, HashMap.class))
                    .orElse(null);
        } catch (IOException e) {
            log.error("读取[{}]文件失败，e={}", yamlFile, e.getMessage(), e);
            return null;
//            return new HashMap(1);
        }
    }

    /**
     * 从yaml的HashMap中读取出具体的属性值
     *
     * @param properties HashMap
     * @param key        参数key
     * @return String
     */
    private static String getYamlValue(HashMap<?, ?> properties, String key) {
        if (null == properties) {
            return null;
        }
        String[] keySplit = key.split("\\.");
        Object o = properties.get(keySplit[0]);
        if (o == null) {
            return null;
        }
        if (keySplit.length > 1) {
            return getYamlValue((HashMap) o, key.substring(keySplit[0].length() + 1));
        }
        return properties.get(key).toString();
    }

}
