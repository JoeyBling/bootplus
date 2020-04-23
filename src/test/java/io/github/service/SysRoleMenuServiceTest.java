package io.github.service;

import io.github.base.BaseAppTest;
import io.github.util.StringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色菜单测试
 *
 * @author Created by 思伟 on 2020/4/22
 */
public class SysRoleMenuServiceTest extends BaseAppTest {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 根据角色ID，获取菜单ID列表
     */
    @Test
    public void queryMenuIdList() {
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(13L);
        outLog(menuIdList);
        menuIdList = sysRoleMenuService.queryMenuIdList(13L);
        outLog(menuIdList);
        menuIdList = sysRoleMenuService.queryMenuIdList(13L);
        outLog(menuIdList);
        menuIdList = sysRoleMenuService.queryMenuIdList(13L);
        outLog(menuIdList);
    }

    private void outLog(List<Long> menuIdList) {
        for (Long menuId : menuIdList) {
            log.debug(StringUtils.toString(menuId));
        }
    }

}
