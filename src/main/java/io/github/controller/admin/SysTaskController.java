package io.github.controller.admin;

import io.github.entity.SysTaskEntity;
import io.github.frame.controller.SimpleController;
import io.github.service.SysTaskService;
import io.github.util.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定时任务
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Controller
@RequestMapping(SysTaskController.PATH)
public class SysTaskController extends SimpleController<SysTaskEntity, SysTaskService, SysTaskController> {
    /**
     * Mapping path & auth prefix
     */
    protected final static String PATH = "/admin/sys/task";

    @Override
    public String getPath() {
        return PATH;
    }

    @Override
    protected String getFrameModuleName() {
        return "定时任务";
    }

    @Override
    @RequiresPermissions("sys:task:list")
    public String showList(SysTaskEntity taskEntity, Model model) {
        return super.showList(taskEntity, model);
    }

    @Override
    @RequiresPermissions("sys:task:list")
    public R list(Integer offset, Integer limit, String sort, String order, SysTaskEntity taskEntity) {
        return super.list(offset, limit, sort, order, taskEntity);
    }

    /**
     * 比较特殊，重写也要加上注解`@PathVariable`
     * 后面考虑不用这个
     */
    @Override
    @RequiresPermissions("sys:task:select")
    public R info(@PathVariable("id") Long id) {
        return super.info(id);
    }

    @Override
    @RequiresPermissions("sys:task:update")
    public R updateEnable(Long id, Boolean enable) {
        return super.updateEnable(id, enable);
    }

    @Override
    @RequiresPermissions("sys:task:delete")
    public R delete(String ids) {
        return super.delete(ids);
    }

}
