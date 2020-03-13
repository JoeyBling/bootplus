package io.github.config.aop.annotation;

import io.github.config.aop.MyControllerRegistry;

import java.lang.annotation.*;

/**
 * 自定义Controller注解（用于Spring动态代理手动注册Controller）
 * 同等于@Controller的效果
 *
 * @author Created by 思伟 on 2020/1/16
 * @see MyControllerRegistry
 * @see org.springframework.stereotype.Controller
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited // 可以被继承
public @interface MyController {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    String value() default "";

    /**
     * Test Field
     */
    String test() default "https://github.com/JoeyBling";

}