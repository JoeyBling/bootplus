package io.github.dao;

import io.github.entity.SysRoleEntity;

/**
 * 角色管理
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    /**
     * 更新角色
     *
     * @param role SysRoleEntity
     */
    void updateNoMapper(SysRoleEntity role);

}
