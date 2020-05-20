package io.github.util;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Yaml读取工具类
 *
 * @author Created by 思伟 on 2020/5/19
 */
@Slf4j
public class YamlUtil {

    /**
     * 获取对应key的值
     *
     * @param propertyKey 参数key
     * @return String
     * @throws IOException
     */
    public static String getProperty(String propertyKey) throws IOException {
        return getYamlValue(getActiveProperties(), propertyKey);
    }

    /**
     * 获取当前活动的配置
     *
     * @return
     * @throws IOException
     */
    private static HashMap getActiveProperties() throws IOException {
        // TODO do cache
        HashMap masterMap = loadYamlPropertiesByCache("application.yml");
        String active = getYamlValue(masterMap, "spring.profiles.active");
        if (null == active || "".equals(active)) {
            return masterMap;
        }
        String activeYaml = "application-" + active + ".yml";
        // 不存在`active`指定的文件则使用默认YAML文件
        return Optional.ofNullable(loadYamlPropertiesByCache(activeYaml)).orElseGet(() -> {
            log.warn("不存在`active`[{}]指定的文件,使用默认主Yaml文件...", active);
            return masterMap;
        });
    }

    /**
     * Yaml配置缓存集合
     */
    protected static Map<String, HashMap<?, ?>> yamlPropertiesCacheMap = new HashMap<String, HashMap<?, ?>>();

    /**
     * 从缓存获取Yaml配置
     *
     * @see #loadYamlProperties(String)
     */
    private static HashMap<?, ?> loadYamlPropertiesByCache(String yamlFile) throws IOException {
        HashMap<?, ?> yamlProperties = yamlPropertiesCacheMap.get(yamlFile);
        if (null == yamlProperties) {
            yamlProperties = loadYamlProperties(yamlFile);
            yamlPropertiesCacheMap.put(yamlFile, yamlProperties);
        }
        return yamlProperties;
    }

    /**
     * 将yaml文件加载为HashMap
     */
    private static HashMap<?, ?> loadYamlProperties(String yamlFile) throws IOException {
        try (InputStream is = YamlUtil.class.getClassLoader().getResourceAsStream(yamlFile)) {
            // 防止文件不存在读取错误
            return Optional.ofNullable(is).map(inputStream -> new Yaml().loadAs(is, HashMap.class))
                    .orElse(null);
        }
    }

    /**
     * 从yaml的HashMap中读取出具体的属性值
     *
     * @param properties HashMap
     * @param key        参数key
     * @return String
     */
    private static String getYamlValue(HashMap properties, String key) {
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
