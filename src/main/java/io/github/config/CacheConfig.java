package io.github.config;

import io.github.config.aop.annotation.MyAutowired;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Spring Cache的一些配置，建议组件相关配置都放在相应的configuration类中
 * MARK:当执行到配置类解析的时候，@Component，@Service，@Controller ,@Configuration标注的类已经全部扫描，
 * 所以这些BeanDifinition已经被同步。 但是bean1的条件注解依赖的是bean2，bean2是被定义的配置类中的，
 * 所以此时配置类的解析无法保证先后顺序，就会出现不生效的情况。
 * Mark:@Order、@AutoConfigureAfter 无用
 * 对于三大注解的正确使用姿势是应该是：请使用在你的自动配置里（一般是你自定义starter时使用），而不是使用在你业务工程中的@Configuration里，因为那会毫无效果。
 * 参考文章：https://mp.weixin.qq.com/s/bNQMoTyrr8pYEHYdIjHW8A
 * META-INF/spring.factories
 * 你可以简单粗暴的记住结论：@AutoConfigureBefore、@AutoConfigureAfter、@AutoConfigureOrder这三个注解只能作用于自动配置类，而不能是自定义的@Configuration配置类。
 *
 * @author Created by 思伟 on 2020/4/23
 */
@Slf4j
@Configuration
@EnableCaching(proxyTargetClass = true)
//@ConditionalOnBean({EhCacheCacheManager.class})
@ConditionalOnClass({EhCacheCacheManager.class})
@AutoConfigureAfter(EhCacheConfiguration.class)
public class CacheConfig extends CachingConfigurerSupport {

    //    @MyAutowired
    private EhCacheCacheManager ehCacheCacheManager;

    /**
     * 重写这个方法，目的是用以提供默认的cacheManager
     */
    @Override
    public CacheManager cacheManager() {
        return super.cacheManager();
//        return ehCacheCacheManager;
    }

    /**
     * 如果cache出错， 我们会记录在日志里，方便排查，比如反序列化异常
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    /**
     * 缓存异常日志处理器
     */
    /* non-public */ static class LoggingCacheErrorHandler extends SimpleCacheErrorHandler {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            logger.error(String.format("cacheName:%s,cacheKey:%s",
                    cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCacheGetError(exception, cache, key);
        }

        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
                                        Object value) {
            logger.error(String.format("cacheName:%s,cacheKey:%s",
                    cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCachePutError(exception, cache, key, value);
        }

        @Override
        public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
            logger.error(String.format("cacheName:%s,cacheKey:%s",
                    cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCacheEvictError(exception, cache, key);
        }

        @Override
        public void handleCacheClearError(RuntimeException exception, Cache cache) {
            logger.error(String.format("cacheName:%s", cache == null ? "unknown" : cache.getName()),
                    exception);
            super.handleCacheClearError(exception, cache);
        }
    }

}
