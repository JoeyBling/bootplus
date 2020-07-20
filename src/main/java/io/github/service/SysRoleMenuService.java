package io.github.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author Created by 思伟 on 2020/6/6
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    /**
     * 保存角色或更新角色
     *
     * @param roleId     Long
     * @param menuIdList List<Long>
     */
    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 清除缓存
     *
     * @param roleId 角色ID
     */
    void clearMenuIdList(Long roleId);

}
