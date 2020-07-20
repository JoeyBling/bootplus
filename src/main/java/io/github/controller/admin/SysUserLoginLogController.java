package io.github.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.entity.SysUserLoginLogEntity;
import io.github.frame.controller.AbstractController;
import io.github.service.SysUserLoginLogService;
import io.github.util.PageUtils;
import io.github.util.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户登录日志
 *
 * @author Created by 思伟 on 2020/6/6
 */
@RestController
@RequestMapping("/admin/sys/log")
public class SysUserLoginLogController extends AbstractController {

    @Resource
    private SysUserLoginLogService sysUserLoginLogService;

    @RequestMapping("/list")
    public R list(Integer offset, Integer limit, String sort, String order,
                  @RequestParam(name = "search", required = false) String loginIp, HttpServletRequest request) throws Exception {
        offset = (getOffset(offset) / getLimit(limit)) + 1;
        // 排序逻辑
        Boolean flag = isOrderByAsc(order);
        Page<SysUserLoginLogEntity> self = sysUserLoginLogService.getPage(offset, getLimit(limit), getAdminId(), loginIp, sort,
                flag);
        return R.ok().put("page", PageUtils.builder()
                .list(self.getRecords())
                .totalCount(self.getTotal())
                .pageSize(self.getSize())
                .currPage(self.getCurrent()).build());
    }

}
