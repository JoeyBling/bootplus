package io.github.config.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义日志注解
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited // 可以被继承
public @interface MyLog {

    String value() default "https://github.com/JoeyBling";

}