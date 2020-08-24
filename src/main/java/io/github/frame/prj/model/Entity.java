package io.github.frame.prj.model;

import java.io.Serializable;

/**
 * 基础实体类接口(建议所有实体类都实现此接口类)
 *
 * @author Created by 思伟 on 2020/8/24
 */
public interface Entity extends Serializable {

    /**
     * 获取所属模块
     *
     * @return String
     */
    String getModule();

    /**
     * 是否为新数据
     *
     * @return boolean
     */
    boolean isNew();
}
