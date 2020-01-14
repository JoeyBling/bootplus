package io.github.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 日志AOP切入
 *
 * @author Created by 思伟 on 2020/1/10
 */
@Aspect
@Component
@Slf4j
@Order(-10) // 保证该AOP在@Transactional之前执行
public class LoggingAspect {

    /**
     * 定义一个 Pointcut, 使用 切点表达式函数 来描述对哪些 Join point 使用 advise.
     * within用于匹配指定类型内的方法执行
     */
    @Pointcut("within(io.github.controller.*.*)")
    public void controllerLog() {
    }

    /**
     * 测试service代理是否正常可用（很奇怪，加了这个注解和logMethodWithServiceLog就可以正常被代理）
     */
//    @Pointcut("within(io.github.service.*.*)")
    public void serviceLog() {
    }

    /**
     * 定义 advise
     */
//    @Before("serviceLog()")
    public void logMethodWithServiceLog(JoinPoint joinPoint) {
        log.debug("代理对象是否是代理调用:{}", AopUtils.isAopProxy(joinPoint.getThis()));
        log.debug("代理对象是否是cglib类代理调用:{}", AopUtils.isCglibProxy(joinPoint.getThis()));
        log.debug("代理对象是否是jdk动态接口代理调用:{}", AopUtils.isJdkDynamicProxy(joinPoint.getThis()));
//        log.debug("被代理对象是否是代理调用:{}", AopUtils.isAopProxy(joinPoint.getTarget()));
//        log.debug("被代理对象是否是cglib类代理调用:{}", AopUtils.isCglibProxy(joinPoint.getTarget()));
//        log.debug("被代理对象是否是jdk动态接口代理调用:{}", AopUtils.isJdkDynamicProxy(joinPoint.getTarget()));
        log.info("---Before method {} invoke, param: {}---", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    /**
     * 指定切点(匹配`io.github.controller`包及其子包下的所有类的所有方法)
     * execution用于匹配方法执行的连接点
     */
    @Pointcut("execution(* io.github.controller.*.*(..))")
    public void optLog() {
    }

    /**
     * 指定切点 前置增强方法(annotation用于匹配当前执行方法持有指定注解的方法)
     */
    @Before("@annotation(myLog)")
    public void myLog(JoinPoint joinPoint, MyLog myLog) {
        log.info("拦截自定义注解==={}", myLog.value());
    }

    /**
     * 签名表达式
     */
    @Pointcut(value = "@annotation(io.github.config.aop.MyLog)")
    public void opLogPointcut() {
    }

    @Before(value = "opLogPointcut() && @annotation(myLog)")
    public void myLogByPointcut(JoinPoint point, MyLog myLog) throws Throwable {
        log.info("拦截自定义注解（第二种方法）==={}", myLog.value());
    }

    /**
     * 定义 advise
     */
    @Before("controllerLog()")
    public void logMethodInvokeParam(JoinPoint joinPoint) {
//        log.info("---Before method {} invoke, param: {}---", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerLog()", returning = "retVal")
    public void logMethodInvokeResult(JoinPoint joinPoint, Object retVal) {
//        log.info("---After method {} invoke, result: {}---", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "controllerLog()", throwing = "exception")
    public void logMethodInvokeException(JoinPoint joinPoint, Exception exception) {
//        log.info("---method {} invoke exception: {}---", joinPoint.getSignature().toShortString(), exception.getMessage());
    }


}
