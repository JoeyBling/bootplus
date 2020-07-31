package io.github.util;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Map自定义工具类
 * 目前功能并不全，待完善
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Created by 思伟 on 2020/6/3
 * @see cn.hutool.core.map.MapUtil
 */
public class MapUtil<K, V> {
    /**
     * 默认初始大小
     */
    public static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * 实例化HashMap
     *
     * @see #DEFAULT_INITIAL_CAPACITY
     * @see #newHashMap(int)
     */
    public static <K, V> MapUtil<K, V> newHashMap() {
        return newHashMap(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * 实例化HashMap
     *
     * @param size 初始容量大小
     * @param <K>
     * @param <V>
     * @return MapUtil
     */
    public static <K, V> MapUtil<K, V> newHashMap(int size) {
        return new MapUtil<K, V>(new HashMap<K, V>(size));
    }

    /**
     * 唯一私有构造函数
     *
     * @param map
     */
    private MapUtil(Map<K, V> map) {
        Assert.notNull(map, "map must not be null");
        this.map = map;
    }

    /**
     * 添加至集合前判断不为空
     *
     * @param key   键
     * @param value 值
     * @return MapUtil
     */
    public MapUtil<K, V> putPreNotNull(K key, V value) {
        if (ObjectUtil.isNotNull(value)) {
            putToMap(key, value);
        }
        return this;
    }

    /**
     * 添加至集合前判断不为空(支持不为空字符串)
     */
    public MapUtil<K, V> putPreNotEmpty(K key, V value) {
        if (ObjectUtil.isNotEmpty(value)) {
            putToMap(key, value);
        }
        return this;
    }

    /**
     * 添加键值对到集合
     */
    public MapUtil<K, V> put(K key, V value) {
        putToMap(key, value);
        return this;
    }

    /**
     * 集合对象
     */
    private Map<K, V> map = null;

    /**
     * 添加键值对到集合
     *
     * @param key   键
     * @param value 值
     * @return 集合对象
     */
    private Map<K, V> putToMap(K key, V value) {
        this.map.put(key, value);
        return this.map;
    }

    /**
     * 获取集合对象
     *
     * @return Map
     */
    public Map<K, V> getMap() {
        return map;
    }

}
