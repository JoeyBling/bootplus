package io.github.frame.prj.service;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.config.aop.service.BaseAopService;
import io.github.frame.prj.model.BaseEntity;
import io.github.util.ObjectId;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * 基础实体类公用Service
 *
 * @author Created by 思伟 on 2020/8/24
 * @see BaseEntity
 */
public abstract class SimpleService<S extends ISimpleService<T>, M extends BaseMapper<T>, T extends BaseEntity>
        extends BaseAopService<S, M, T> implements ISimpleService<T> {

    /**
     * 解决子类调用父类字段进行构造查询等问题
     *
     * @url { https://github.com/baomidou/mybatis-plus/issues/1935 }
     * @url { https://github.com/baomidou/mybatis-plus/issues/2616 }
     * @see AbstractLambdaWrapper#tryInitCache
     */
    private T entity;

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public SimpleService() {
        Class clazz = getClass();
        try {
            // 处理有继承的情况，有继承时，取父类（ParameterizedType = 泛型实例）
            while (!ParameterizedType.class.isAssignableFrom(clazz.getGenericSuperclass().getClass())
                    && !clazz.equals(Object.class)) {
                clazz = clazz.getSuperclass();
            }
            // 取出第3个参数的实际类型（实体类对象类型）
            Class<T> entityClass = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[2];
            entity = entityClass.newInstance();
        } catch (Throwable e) {
            logger.error("{}注入entity失败，请手动重写`setEntity()`方法，e={}", clazz, e.getMessage());
        }
    }

    /**
     * 是否仅查询有效数据，即list，select只返回enabled的数据
     *
     * @return true
     */
    protected boolean onlyEnabled() {
        return true;
    }

    /**
     * 查询全部有效的数据
     *
     * @return List
     */
    @Override
    public List<T> listEnabledAll() {
        return super.list(Wrappers.<T>lambdaQuery(entity)
                .eq(T::getEnabled, true));
    }

    @Override
    public boolean enableByPrimaryKey(Long id) {
        return updateEnable(id, true);
    }

    /**
     * 根据主键ID未启用数据
     *
     * @param id ID
     * @return boolean
     */
    @Override
    public boolean disableByPrimaryKey(Long id) {
        return updateEnable(id, false);
    }

    @Override
    public boolean updateEnable(Long id, boolean enabled) {
        try {
            final T defT = (T) entity.getClass().newInstance();
            defT.setId(id);
            defT.setEnabled(enabled);
            return super.updateById(defT);
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
    }

    @Override
    public <E extends IPage<T>> E page(E page, Wrapper<T> queryWrapper) {
        if (onlyEnabled()) {
            if (null == queryWrapper) {
                queryWrapper = Wrappers.lambdaQuery(entity).eq(T::getEnabled, true);
            } else {
                // https://github.com/baomidou/mybatis-plus/issues/2841
                ((QueryWrapper<T>) queryWrapper).lambda().eq(T::getEnabled, true);
            }
        }
        return super.page(page, queryWrapper);
    }

    @Override
    public <E extends IPage<T>> E page(E page) {
        return page(page, Wrappers.emptyWrapper());
    }

    @Override
    public boolean save(T entity) {
        set4Insert(entity);
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        for (T t : entityList) {
            set4Insert(t);
        }
        return super.saveBatch(entityList, batchSize);
    }

    /**
     * 插入前默认值
     *
     * @param entity T
     */
    private void set4Insert(T entity) {
        if (null == entity.getId()) {
            entity.setId(ObjectId.uniqId());
        }
        if (null == entity.getEnabled()) {
            entity.setEnabled(true);
        }
    }

}
