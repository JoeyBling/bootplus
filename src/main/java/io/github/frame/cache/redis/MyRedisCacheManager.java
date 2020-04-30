package io.github.frame.cache.redis;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 重写Redis 缓存Manager
 *
 * @author Created by 思伟 on 2020/4/27
 * @see RedisCacheManager
 */
public class MyRedisCacheManager extends RedisCacheManager {

    /**
     * A redis template
     */
    protected RedisTemplate redisTemplate;

    /**
     * 缓存是否允许空值键
     */
    protected final boolean cacheNullValues;

    /**
     * 缓存对象集合
     * TODO ? extends
     */
    protected ConcurrentMap<String, RedisCache> cacheMap = new ConcurrentHashMap<String, RedisCache>(16);

    /**
     * 缓存的时间
     */
    protected Map<String, Integer> expireMap = new HashMap<String, Integer>(16);

    public MyRedisCacheManager(RedisTemplate redisTemplate) {
        this(redisTemplate, false);
    }

    public MyRedisCacheManager(RedisTemplate redisTemplate, boolean cacheNullValues) {
        this((RedisOperations) redisTemplate, cacheNullValues);
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
        this.redisTemplate = redisTemplate;
    }

    public MyRedisCacheManager(RedisOperations redisOperations) {
        this(redisOperations, Collections.<String>emptyList());
    }

    public MyRedisCacheManager(RedisOperations redisOperations, boolean cacheNullValues) {
        this(redisOperations, Collections.<String>emptyList(), cacheNullValues);
    }

    public MyRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        this(redisOperations, cacheNames, false);
    }

    public MyRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames,
                               boolean cacheNullValues) {
        super(redisOperations, cacheNames, cacheNullValues);
        this.cacheNullValues = cacheNullValues;
    }

    public void setConfigMap(Map<String, Integer> configMap) {
        this.expireMap = configMap;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<? extends Cache> values = super.loadCaches();
//        Collection<Cache> values = cacheMap.values();
        return values;
    }

    @Override
    protected RedisCache createCache(String cacheName) {
        RedisCache cache = cacheMap.get(cacheName);
        if (cache == null) {
            // Fully synchronize now for missing cache creation...
            synchronized (this.cacheMap) {
                Integer expire = expireMap.get(cacheName);
                if (expire == null) {
                    // 默认1小时
                    expire = 1 * 60 * 60;
                    expireMap.put(cacheName, expire);
                }
                long expiration = computeExpiration(cacheName);
                cache = new MyRedisCache(redisTemplate, cacheName, expire,
                        (isUsePrefix() ? getCachePrefix().prefix(cacheName) : null),
                        expiration, cacheNullValues);
                if (null != cache) {
                    cacheMap.put(cacheName, cache);
                }
            }
        }
        return cache;
    }

}
