package io.github.frame.cache.redis;

import io.github.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 重写Redis 缓存
 *
 * @author Created by 思伟 on 2020/4/27
 * @see RedisCache,MyRedisCacheManager
 */
public class MyRedisCache extends RedisCache {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private RedisTemplate<? extends Object, ? extends Object> redisTemplate;

    private String name;

    private int expire = 0;


    public MyRedisCache(RedisTemplate redisTemplate, String name, byte[] prefix, long expiration, boolean allowNullValues) {
        super(name, prefix, redisTemplate, expiration, allowNullValues);
        this.redisTemplate = redisTemplate;
        this.name = name;
    }

    public MyRedisCache(RedisTemplate redisTemplate, String name, byte[] prefix, long expiration) {
        this(redisTemplate, name, prefix, expiration, false);
    }

    public MyRedisCache(RedisTemplate redisTemplate, String name, int expire,
                        byte[] prefix, long expiration, boolean allowNullValues) {
        this(redisTemplate, name, prefix, expiration, allowNullValues);
        this.expire = expire;
    }

    public RedisTemplate<? extends Object, ? extends Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<? extends Object, ? extends Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ValueWrapper get(Object key) {
        final String keyStr = toString(key);
        ValueWrapper wrapper = super.get(key);
        if (null != wrapper) {
            log.debug("获取Redis缓存 key ：{}, value : {}",
                    keyStr, toString(wrapper != null ? wrapper.get() : null));
        }
        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        ValueWrapper waWrapper = super.get(key);
        if (null != waWrapper) {
            log.warn("重复添加Redis缓存 key ：{}, old-value : {}", key, toString(waWrapper.get()));
        }
        log.debug("设置Redis缓存 key ：{}", toString(key));
        super.put(key, value);
    }

    @Override
    public void evict(Object key) {
        log.debug("删除Redis缓存 key ：{}", toString(key));
        super.evict(key);
    }

    /**
     * 将对象转换为String
     *
     * @param object Object
     * @return String
     */
    protected String toString(Object object) {
        return StringUtils.toString(object);
    }

}
