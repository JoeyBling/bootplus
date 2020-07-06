package io.github.config.aop.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义Autowired注解（不检测依赖是否强制必须）
 * 忽略当前要注入的bean，如果有直接注入，没有跳过，不会报错
 * 使用此注解必须要判断对象是否为空
 * FIXME IDEA会报警告【Could not autowire. No beans of '*' type found.】
 * 手动关闭警告（不推荐）
 * File -> Settings -> Editor -> Inspections ->
 * Spring -> Spring Core -> Code -> Autowiring for Bean Class
 *
 * @author Created by 思伟 on 2020/3/13
 * @see org.springframework.beans.factory.annotation.Autowired
 * @see NullPointerException
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited // 可以被继承 -- 只对类注解有效，方法注解无效
@Autowired
public @interface MyAutowired {

    /**
     * Declares whether the annotated dependency is required.
     * <p>Defaults to {@code false}.
     */
    @AliasFor(annotation = Autowired.class, attribute = "required")
    boolean required() default false;

}