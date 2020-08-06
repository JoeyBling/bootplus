package io.github.frame.prj.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Api接口方法声明
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiMethod {

    /**
     * Alias for {@link #name}.
     */
    @AliasFor(attribute = "name")
    String[] value() default {};

    /**
     * 接口名
     *
     * @return String[]
     */
    String[] name() default {};

    /**
     * 接口调用权限(为空不鉴权)
     *
     * @return String[]
     */
    String[] permission() default {};
}
