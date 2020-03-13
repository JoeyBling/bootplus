package io.github.config.aop.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 自定义Service注解(同时导入事务注解)
 *
 * @author Created by 思伟 on 2020/3/13
 * @see Service
 * @see Transactional
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited // 可以被继承
@Service
@Transactional
public @interface MyService {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    @AliasFor(annotation = Service.class, attribute = "value")
    String value() default "";

    /**
     * @see org.springframework.transaction.interceptor.TransactionAttribute#isReadOnly()
     */
    @AliasFor(annotation = Transactional.class, attribute = "readOnly")
    boolean readOnly() default false;

    @AliasFor(annotation = Transactional.class, attribute = "rollbackFor")
    Class<? extends Throwable>[] rollbackFor() default {};

}