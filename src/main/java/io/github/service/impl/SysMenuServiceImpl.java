package io.github.service.impl;

import io.github.config.ApplicationProperties;
import io.github.config.aop.service.BaseAopService;
import io.github.dao.SysMenuDao;
import io.github.entity.SysMenuEntity;
import io.github.entity.enums.SysMenuTypeEnum;
import io.github.frame.cache.annotation.MyCacheEvict;
import io.github.frame.cache.annotation.MyCacheable;
import io.github.service.SysMenuService;
import io.github.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单管理
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Service
public class SysMenuServiceImpl extends BaseAopService<SysMenuServiceImpl, SysMenuDao, SysMenuEntity>
        implements SysMenuService {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private ApplicationProperties applicationProperties;

    @Override
    public String getCachePrefix() {
        return "SYS_MENU";
    }

    @Override
    public List<SysMenuEntity> queryList(Map<String, Object> map) {
        return baseMapper.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return baseMapper.queryTotal(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Long[] menuIds) {
        return baseMapper.deleteBatch(menuIds);
    }

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = baseMapper.queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }
        // 过滤不存在的用户菜单
        return menuList.stream().filter(menu ->
                menuIdList.contains(menu.getMenuId())).collect(Collectors.toList());
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    @Override
    @MyCacheable(key = "#root.target.cachePrefix + '_LIST_' + #userId")
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        List<SysMenuEntity> allMenuList;
        // 系统管理员，拥有最高权限（全部菜单可看）
        if (applicationProperties.getAdminId().equals(userId)) {
            allMenuList = getAllMenuList(null);
        } else {
            // 用户菜单列表
            List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
            allMenuList = getAllMenuList(menuIdList);
        }
        return allMenuList;
    }

    @Override
    @MyCacheEvict(key = "#root.target.cachePrefix + '_LIST_' + #userId")
    public void clearUserMenuList(Long userId) {
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        // 查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        // 递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
        for (SysMenuEntity entity : menuList) {
            // 目录
            if (entity.getType() == SysMenuTypeEnum.CATALOG) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }
}
