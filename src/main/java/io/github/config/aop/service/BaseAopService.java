package io.github.config.aop.service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.github.util.exception.SysRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 所有service必须继承此类来解决内部方法调用事务失效问题
 * （ 泛型：S为当前对象，M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 * TODO 后面写个单独类，实现的接口都可以取得AOP self对象属性
 *
 * @author Created by 思伟 on 2020/1/13
 * @see MyInjectBeanSelfProcessor#postProcessAfterInitialization(Object, String)
 */
public abstract class BaseAopService<S, M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BeanSelfAware {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * self会在编译的时候和this具有相同的效果，而this是关键字
     * 使用内部方法时可以指定self... 或者不加关键字即可使用代理对象(勿使用this不然无效)
     * 不加关键字测试了好像不行，建议使用self
     */
    protected S self;

    @SuppressWarnings("unchecked")
    @Override
    public void setSelf(Object proxyBean) {
        // Class clazz = getClass();
        // 处理有继承的情况，有继承时，取父类
//        while (!ParameterizedType.class.isAssignableFrom(clazz.getGenericSuperclass().getClass())
//                && !clazz.equals(Object.class)) {
//            clazz = clazz.getSuperclass();
//        }
//        Class serviceClass = (Class<S>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        if (null != proxyBean) {
            this.self = (S) proxyBean;
        }
    }

    /**
     * 自定义缓存Key前缀
     * 如果要使用缓存注解：#root.target.cachePrefix 必须实现此方法
     *
     * @return String
     */
    public String getCachePrefix() {
        throw new SysRuntimeException("请重写getCachePrefix方法");
    }

}
