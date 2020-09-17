package io.github.frame.prj.service;

/**
 * Api接口
 * 目前暂不实现动态代理手动注册Spring容器bean
 *
 * @author Created by 思伟 on 2020/8/3
 */
public interface ApiService {

    /**
     * 接口调用权限(为空不鉴权)默认先判断方法声明权限
     *
     * @return 此处可以设置整个接口类默认的鉴权模式
     */
    default String[] getPermission() {
        return null;
    }

}
