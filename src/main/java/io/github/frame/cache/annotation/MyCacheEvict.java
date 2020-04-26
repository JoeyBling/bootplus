package io.github.frame.cache.annotation;

import io.github.frame.cache.CacheNameProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义缓存注解
 * 实现默认缓存名
 *
 * @author Created by 思伟 on 2020/4/26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CacheEvict
public @interface MyCacheEvict {

    /**
     * @see CacheEvict#value()
     */
    @AliasFor(annotation = CacheEvict.class, attribute = "value")
    String[] value() default {CacheNameProperty.NORMAL};

    /**
     * @see CacheEvict#key()
     */
    @AliasFor(annotation = CacheEvict.class, attribute = "key")
    String key() default "";

    /**
     * @see CacheEvict#cacheManager()
     */
    @AliasFor(annotation = CacheEvict.class, attribute = "cacheManager")
    String cacheManager() default "";
}
