package io.github.frame.spring;

import org.springframework.context.ApplicationContext;

/**
 * 启动接口，在系统启动时，会调用继承此接口的类
 * 一般用于完成初始化工作
 * 注意实现此接口的类，必须被spring管理
 *
 * @author Created by 思伟 on 2020/4/23
 * @FunctionalInterface 是在 Java 8 中添加的一个新注解，用于指示接口类型，声明接口为 Java 语言规范定义的功能接口
 * 一旦定义了功能接口，我们就可以利用 `Lambda` 表达式调用
 */
@FunctionalInterface
public interface IStartUp {

    /**
     * 初始化启动
     *
     * @param applicationContext ApplicationContext
     * @throws Exception
     */
    void startUp(final ApplicationContext applicationContext) throws Exception;

}
