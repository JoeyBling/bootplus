package io.github.frame.spring;

import org.springframework.context.ApplicationContext;

/**
 * 启动接口，在系统启动时，会调用继承此接口的类
 * 一般用于完成初始化工作
 * 注意实现此接口的类，必须被spring管理
 *
 * @author Created by 思伟 on 2020/4/23
 */
public interface IStartUp {

    /**
     * 初始化启动
     *
     * @param applicationContext ApplicationContext
     * @throws Exception
     */
    void startUp(ApplicationContext applicationContext) throws Exception;

}
