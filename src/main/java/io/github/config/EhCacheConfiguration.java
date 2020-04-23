package io.github.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * EhCache缓存管理器
 *
 * @author Joey
 * @Email 2434387555@qq.com
 * @see org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
 */
@Slf4j
@Deprecated
@Configuration
public class EhCacheConfiguration {

    /**
     * ehcache 主要的管理器
     *
     * @param bean EhCacheManagerFactoryBean
     * @return EhCacheCacheManager
     */
//    @Bean(name = "coreEhCacheCacheManager")
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
        return new EhCacheCacheManager(bean.getObject());
    }

    /**
     * 据shared与否的设置,Spring分别通过CacheManager.create()或new CacheManager()方式来创建一个ehcache基地.
     *
     * @return EhCacheManagerFactoryBean
     */
//    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        log.debug("EhCacheConfiguration.ehCacheManagerFactoryBean()...");
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache-core.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

}
