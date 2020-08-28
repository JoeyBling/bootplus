package io.github.frame.prj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.frame.prj.model.BaseEntity;

import java.util.List;

/**
 * simple模式service接口类
 *
 * @author Created by 思伟 on 2020/8/24
 * @see SimpleService
 */
public interface ISimpleService<T extends BaseEntity> extends IService<T> {

    /**
     * 查询列表
     *
     * @param offset 开始
     * @param limit  条数
     * @param sort   排序字段
     * @param isAsc  是否为升序
     * @param entity 查询条件
     * @return Page
     */
    Page<T> getPage(Integer offset, Integer limit, String sort, Boolean isAsc, T entity);

    /**
     * 查询全部有效的数据
     *
     * @return List
     */
    List<T> listEnabledAll();

    /**
     * 根据主键ID启用数据
     *
     * @param id ID
     * @return boolean
     */
    boolean enableByPrimaryKey(Long id);

    /**
     * 根据主键ID未启用数据
     *
     * @param id ID
     * @return boolean
     */
    boolean disableByPrimaryKey(Long id);

    /**
     * 根据ID更新启用状态
     *
     * @param id      ID
     * @param enabled 状态
     * @return boolean
     */
    boolean updateEnable(Long id, boolean enabled);

}
