package io.github.frame.cache.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

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

    protected final RedisCacheWriter cacheWriter;
    protected final RedisCacheConfiguration defaultCacheConfig;

    /**
     * 缓存对象集合
     */
    protected ConcurrentMap<String, RedisCache> cacheMap = new ConcurrentHashMap<String, RedisCache>(16);

    /**
     * 缓存的时间
     */
    protected Map<String, Integer> expireMap = new HashMap<String, Integer>(16);

    public MyRedisCacheManager(RedisTemplate redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        this(cacheWriter, defaultCacheConfiguration);
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
        this.redisTemplate = redisTemplate;
    }

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public void setConfigMap(Map<String, Integer> configMap) {
        this.expireMap = configMap;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        RedisCache cache = cacheMap.get(name);
        if (cache == null) {
            cache = new MyRedisCache(redisTemplate, name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
            if (null != cache) {
                cacheMap.put(name, cache);
            }
        }
        return cache;
    }

}
