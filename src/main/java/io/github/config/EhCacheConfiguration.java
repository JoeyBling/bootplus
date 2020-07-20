package io.github.config;

import cn.hutool.core.collection.CollectionUtil;
import io.github.frame.cache.ehcache.MyEhCacheCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * EhCache缓存管理器
 *
 * @author Created by 思伟 on 2020/6/6
 * @see org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
 */
@Slf4j
@Configuration
public class EhCacheConfiguration {

    /**
     * 据shared与否的设置,Spring分别通过CacheManager.create()或new CacheManager()方式来创建一个ehcache基地.
     *
     * @return EhCacheManagerFactoryBean
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        log.debug("EhCacheConfiguration.ehCacheManagerFactoryBean()...");
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache-core.xml"));
        cacheManagerFactoryBean.setShared(true);
//        cacheManagerFactoryBean.setCacheManagerName("BOOT_PLUS_CORE");
        return cacheManagerFactoryBean;
    }

    /**
     * EhCache 主要的管理器
     *
     * @param bean EhCacheManagerFactoryBean
     * @return EhCacheCacheManager
     */
    @Bean(name = "coreEhCacheCacheManager")
    @ConditionalOnBean(EhCacheManagerFactoryBean.class)
    @Primary
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
        if (log.isDebugEnabled()) {
            CollectionUtil.newArrayList(bean.getObject().getCacheNames()).forEach(ehcacheName -> {
                log.debug("EhCache--ehcacheName=【{}】", ehcacheName);
            });
        }
        // 自定义 EhCache 缓存Manager
        EhCacheCacheManager ehCacheCacheManager = new MyEhCacheCacheManager(bean.getObject());
//        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(bean.getObject());
        return ehCacheCacheManager;
    }

}
