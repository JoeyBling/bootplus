package io.github.frame.prj.service;

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
     * 查询全部有效的数据
     *
     * @return List
     */
    List<T> listEnabledAll();

    /**
     * 根据主键ID未启用数据
     *
     * @param id ID
     * @return boolean
     */
    boolean disableByPrimaryKey(Long id);

}
