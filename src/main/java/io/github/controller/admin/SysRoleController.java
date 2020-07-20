package io.github.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.frame.controller.AbstractController;
import io.github.entity.SysRoleEntity;
import io.github.service.SysRoleMenuService;
import io.github.service.SysRoleService;
import io.github.util.DateUtils;
import io.github.util.JoeyUtil;
import io.github.util.PageUtils;
import io.github.util.R;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 角色管理
 *
 * @author Created by 思伟 on 2020/6/6
 */
@RestController
@RequestMapping("/admin/sys/role")
public class SysRoleController extends AbstractController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public R list(Integer offset, Integer limit, String sort, String order,
                  @RequestParam(name = "search", required = false) String roleName) {
        offset = (offset / limit) + 1;
        // 排序逻辑
        Boolean flag = isOrderByAsc(order);
        Page<SysRoleEntity> roleList = sysRoleService.queryListByPage(offset, limit, roleName, sort, flag);
        PageUtils pageUtil = new PageUtils(roleList.getRecords(), roleList.getTotal(), roleList.getSize(),
                roleList.getCurrent());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:role:select")
    public R select() {
        // 查询列表数据
        List<SysRoleEntity> list = sysRoleService.queryList(new HashMap<String, Object>(0));

        return R.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public R info(@PathVariable("roleId") Long roleId) {
        SysRoleEntity role = sysRoleService.getById(roleId);

        // 查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        return R.ok().put("role", role);
    }

    /**
     * 保存角色
     *
     * @throws ParseException
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(String roleName, String remark, @RequestParam("menuIds") String ids) throws ParseException {
        if (StringUtils.isBlank(roleName)) {
            return R.error("角色名称不能为空");
        }
        SysRoleEntity roleEntity = new SysRoleEntity();
        roleEntity.setRoleName(roleName);
        roleEntity.setRemark(remark);
        roleEntity.setCreateTime(JoeyUtil.stampDate(new Date(), DateUtils.DATE_TIME_PATTERN));

        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] menuId = toArrays(jsonArray);
        List<Long> menuIds = new ArrayList<Long>();
        Collections.addAll(menuIds, menuId);
        if (menuIds.size() < 0) {
            return R.error("请为角色授权");
        }
        roleEntity.setMenuIdList(menuIds);
        sysRoleService.save(roleEntity);
        return R.ok();
    }

    /**
     * 修改角色
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public R update(Long roleId, String roleName, String remark, @RequestParam("menuIds") String ids) {
        if (StringUtils.isBlank(roleName)) {
            return R.error("角色名称不能为空");
        }
        SysRoleEntity roleEntity = new SysRoleEntity();
        roleEntity.setRoleId(roleId);
        roleEntity.setRoleName(roleName);
        roleEntity.setRemark(remark);

        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] menuId = toArrays(jsonArray);
        List<Long> menuIds = new ArrayList<Long>();
        Collections.addAll(menuIds, menuId);
        if (menuIds.size() < 0) {
            return R.error("请为角色授权");
        }
        roleEntity.setMenuIdList(menuIds);

        sysRoleService.update(roleEntity);

        return R.ok();
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestParam("roleIds") String ids) {
        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] roleIds = toArrays(jsonArray);
        sysRoleService.deleteBatch(roleIds);
        return R.ok();
    }

}
