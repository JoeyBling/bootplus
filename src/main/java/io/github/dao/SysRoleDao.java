package io.github.dao;

import io.github.entity.SysRoleEntity;
import io.github.util.BaseDao;

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
