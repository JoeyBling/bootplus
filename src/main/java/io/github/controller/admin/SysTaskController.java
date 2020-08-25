package io.github.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.entity.SysTaskEntity;
import io.github.frame.controller.AbstractController;
import io.github.service.SysTaskService;
import io.github.util.PageUtils;
import io.github.util.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

/**
 * 定时任务
 *
 * @author Created by 思伟 on 2020/6/6
 */
@RestController
@RequestMapping(SysTaskController.PATH)
public class SysTaskController extends AbstractController<SysTaskController> {
    /**
     * Mapping path & auth prefix
     */
    protected final static String PATH = "/admin/sys/task";

    @Resource
    private SysTaskService sysTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:task:list")
    public R list(Integer offset, Integer limit, String sort, String order,
                  @RequestParam(name = "search", required = false) String search) {
        offset = (getOffset(offset) / getLimit(limit)) + 1;
        // 排序逻辑
        Boolean flag = isOrderByAsc(order);
        Page<SysTaskEntity> page = sysTaskService.getPage(offset, getLimit(limit), sort, flag,
                Optional.ofNullable(JSON.parseObject(search, SysTaskEntity.class)).orElse(new SysTaskEntity()));
        return R.ok().put("page", PageUtils.buildPageUtil(page));
    }

    /**
     * 详情
     */
    @RequestMapping("/select/{id}")
    @RequiresPermissions("sys:task:select")
    public R info(@PathVariable("id") Long id) {
        SysTaskEntity entity = sysTaskService.getById(id);
        return R.ok().put("record", entity);
    }

    /**
     * 修改状态
     */
    @RequestMapping("/updateEnable")
    @RequiresPermissions("sys:task:update")
    public R updateEnable(Long id, Boolean enable) {
        boolean updateEnable = sysTaskService.updateEnable(id, Boolean.TRUE.equals(enable));
        return updateEnable ? R.ok() : R.error("修改失败!");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:task:delete")
    public R delete(@RequestParam("ids") String ids) {
        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] idArr = toArrays(jsonArray);
        sysTaskService.removeByIds(Arrays.asList(idArr));
        return R.ok();
    }
}
