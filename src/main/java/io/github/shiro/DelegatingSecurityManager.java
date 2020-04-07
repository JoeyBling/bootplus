package io.github.shiro;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * springboot整合shiro @Transactional事务注解不起作用问题解决
 * https://blog.csdn.net/nvisalsd/article/details/104669424
 *
 * @author Created by 思伟 on 2020/4/7
 */
public class DelegatingSecurityManager extends AbstractFactoryBean<WebSecurityManager>
        implements InvocationHandler, BeanClassLoaderAware {

    @Autowired
    @Lazy
    private DefaultWebSecurityManager defaultWebSecurityManager;

    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(proxy, args);
        }
        try {
            return method.invoke(this.defaultWebSecurityManager, args);
        } catch (InvocationTargetException e) {
            // 代理调用，要剥掉最外层的InvocationTargetException
            throw e.getTargetException();
        }
    }

    @Override
    public Class<?> getObjectType() {
        return WebSecurityManager.class;
    }

    @Override
    protected WebSecurityManager createInstance() throws Exception {
        return (WebSecurityManager) Proxy.newProxyInstance(this.classLoader,
                new Class<?>[]{WebSecurityManager.class}, this);
    }

}