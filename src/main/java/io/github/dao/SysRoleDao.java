package io.github.dao;

import io.github.entity.SysRoleEntity;

/**
 * 角色管理
 *
 * @author Created by 思伟 on 2020/6/6
 */
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    /**
     * 更新角色
     *
     * @param role SysRoleEntity
     */
    void updateNoMapper(SysRoleEntity role);

}
