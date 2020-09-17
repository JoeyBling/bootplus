package io.github.frame.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 重写EhCache 缓存Manager
 * CacheManager backed by an EhCache {@link net.sf.ehcache.CacheManager}.
 *
 * @author Updated by 思伟 on 2020/4/23
 * @see org.springframework.cache.ehcache.EhCacheCacheManager
 */
@Slf4j
public class MyEhCacheCacheManager extends EhCacheCacheManager {
    /**
     * 是否允许新缓存创建
     */
    private boolean allowInFlightCacheCreation = true;
    /**
     * 缓存对象集合
     */
    protected final ConcurrentMap<String, Cache> cacheMap =
            new ConcurrentHashMap<String, Cache>(16);

    /**
     * 缓存的时间
     */
    protected Map<String, Integer> expireMap = new ConcurrentHashMap<String, Integer>(16);

    /**
     * Create a new EhCacheCacheManager, setting the target EhCache CacheManager
     * through the {@link #setCacheManager} bean property.
     */
    public MyEhCacheCacheManager() {
    }

    /**
     * Create a new EhCacheCacheManager for the given backing EhCache CacheManager.
     *
     * @param cacheManager the backing EhCache {@link net.sf.ehcache.CacheManager}
     */
    public MyEhCacheCacheManager(net.sf.ehcache.CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache == null) {
            // Fully synchronize now for missing cache creation...
            synchronized (this.cacheMap) {
                Integer expire = expireMap.get(name);
                if (expire == null) {
                    // 默认1小时
                    expire = 1 * 60 * 60;
                    expireMap.put(name, expire);
                }
                cache = getMissingCache(name);
                if (null != cache) {
                    cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    protected Collection<Cache> loadCaches() {
        Status status = getCacheManager().getStatus();
        if (!Status.STATUS_ALIVE.equals(status)) {
            throw new IllegalStateException(
                    "An 'alive' EhCache CacheManager is required - current cache is " + status.toString());
        }
        String[] names = getCacheManager().getCacheNames();
        Collection<Cache> caches = new LinkedHashSet<Cache>(names.length);
        for (String name : names) {
            caches.add(new MyEhCacheCache(getCacheManager().getEhcache(name)));
        }
        return caches;
    }

    @Override
    protected Cache getMissingCache(String name) {
        // Check the EhCache cache again (in case the cache was added at runtime)
        Ehcache ehcache = getCacheManager().getEhcache(name);
        if (ehcache != null) {
            return new MyEhCacheCache(ehcache);
        }
        return allowInFlightCacheCreation ? createCache(name) : null;
    }

    /**
     * 创建一个新的Cache
     * Configuration hook for creating {@link MyEhCacheCache} with given name.
     *
     * @param name must not be {@literal null}.
     * @return never {@literal null}.
     */
    protected Cache createCache(String name) {
        log.info("Create new Ehcache[{}]", name);
        getCacheManager().addCache(name);
        final Ehcache ehcache = getCacheManager().getEhcache(name);
        return new MyEhCacheCache(ehcache);
    }

    public void setConfigMap(Map<String, Integer> configMap) {
        this.expireMap = configMap;
    }

    @Override
    public Collection<String> getCacheNames() {
        return super.getCacheNames();
    }

}
