package io.github.frame.prj.util;

import io.github.entity.SysUserEntity;
import org.hibernate.validator.internal.engine.DefaultClockProvider;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.*;
import java.util.Set;

/**
 * Bean Validation数据校验的认知：https://mp.weixin.qq.com/s/g04HMhrjbvbPn1Mb9JYa5g
 * 1、https://mp.weixin.qq.com/s/MQjXG0cg8domRtwf3ArvHw
 *
 * @author Created by 思伟 on 2020/9/15
 */
public class ValidatorUtil {

    /**
     * 使用【默认配置】得到一个校验工厂
     *
     * @return ValidatorFactory
     */
    public static ValidatorFactory obtainValidatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }

    /**
     * 获取一个默认的校验器
     *
     * @return Validator
     */
    public static Validator obtainValidator() {
        return obtainValidatorFactory().getValidator();
    }

    /**
     * 获取一个默认的验证方法的参数和返回值
     *
     * @return ExecutableValidator
     */
    public static ExecutableValidator obtainExecutableValidator() {
        return obtainValidator().forExecutables();
    }

    /**
     * 简易输出校验结果日志
     *
     * @param violations 返回校验结果
     * @param <T>        T
     */
    @Deprecated
    public static <T> void printViolations(Set<ConstraintViolation<T>> violations) {
        violations.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }

    public static void main(String[] args) {
        final BeanDescriptor beanDescriptor = obtainValidator().getConstraintsForClass(SysUserEntity.class);
        System.out.println("此类是否需要校验：" + beanDescriptor.isBeanConstrained());

        // 获取属性、方法、构造器的约束
        Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
        Set<MethodDescriptor> constrainedMethods = beanDescriptor.getConstrainedMethods(MethodType.GETTER);
        Set<ConstructorDescriptor> constrainedConstructors = beanDescriptor.getConstrainedConstructors();
        System.out.println("需要校验的属性：" + constrainedProperties);
        System.out.println("需要校验的方法：" + constrainedMethods);
        System.out.println("需要校验的构造器：" + constrainedConstructors);

        // Bean Validation e.g.
        final SysUserEntity sysUserEntity = SysUserEntity.builder()
//                .username("Joey")
//                .sex(2)
                .status(true).build();
        // 1、使用【默认配置】得到一个校验工厂  这个配置可以来自于provider、SPI提供
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 2、得到一个校验器
        Validator validator = validatorFactory.getValidator();
        // 3、校验Java Bean（解析注解） 返回校验结果
        Set<ConstraintViolation<SysUserEntity>> result = validator.validate(sysUserEntity);
        // 输出校验结果
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);

        Validator validatorCon = ValidatorUtil.obtainValidatorFactory().usingContext()
                .parameterNameProvider(new DefaultParameterNameProvider())
                .clockProvider(DefaultClockProvider.INSTANCE)
                .getValidator();

    }

}
