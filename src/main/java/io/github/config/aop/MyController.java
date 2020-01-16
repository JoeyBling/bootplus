package io.github.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义Controller注解（用于Spring动态代理手动注册Controller）
 *
 * @author Created by 思伟 on 2020/1/16
 * @see MyControllerRegistry
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyController {

    /**
     * Test Field
     */
    String value() default "https://github.com/JoeyBling";

}