package io.github.frame.cache.ehcache;

import io.github.util.StringUtils;
import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;

/**
 * 重写EhCache 缓存
 * {@link Cache} implementation on top of an {@link Ehcache} instance.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @author Updated by 思伟 on 2020/4/23
 * @see MyEhCacheCacheManager
 * @see org.springframework.cache.ehcache.EhCacheCache
 * @since 3.1
 */
public class MyEhCacheCache extends EhCacheCache {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected final Ehcache ehcache;

    /**
     * Create an {@link MyEhCacheCache} instance.
     *
     * @param ehcache backing Ehcache instance
     */
    public MyEhCacheCache(Ehcache ehcache) {
        super(ehcache);
        this.ehcache = ehcache;
    }

    @Override
    public ValueWrapper get(Object key) {
        final String keyStr = toString(key);
        ValueWrapper wrapper = super.get(key);
        if (null != wrapper) {
            log.debug("获取EhCache缓存 key ：{}, value : {}",
                    keyStr, toString(wrapper != null ? wrapper.get() : null));
        }
        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        ValueWrapper waWrapper = super.get(key);
        if (null != waWrapper) {
            log.warn("重复添加EhCache缓存 key ：{}, old-value : {}", key, toString(waWrapper.get()));
        }
        log.debug("设置EhCache缓存 key ：{}", toString(key));
        super.put(key, value);
    }

    @Override
    public void evict(Object key) {
        log.debug("删除EhCache缓存 key ：{}", toString(key));
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
