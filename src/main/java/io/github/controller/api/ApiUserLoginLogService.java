package io.github.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.github.controller.api.transfer.request.TransUserLoginRequest;
import io.github.entity.SysUserLoginLogEntity;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.frame.prj.constant.UserTypeConst;
import io.github.frame.prj.service.BaseApiService;
import io.github.frame.prj.service.annotation.ApiMethod;
import io.github.frame.prj.service.annotation.Logical;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.frame.prj.transfer.response.TransPageBaseResponse;
import io.github.frame.prj.validate.Validate4Api;
import io.github.frame.prj.validate.Validate4ApiRule;
import io.github.service.SysUserLoginLogService;
import io.github.util.PageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统用户登录日志接口
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Service
public class ApiUserLoginLogService extends BaseApiService {

    @Resource
    private SysUserLoginLogService sysUserLoginLogService;

    /**
     * @api {POST} bootplus.user.login.log.list 02.系统用户登录日志列表
     * @apiPermission ADM 或者 SYS
     * @apiDescription 接口名：<code>bootplus.user.login.log.list</code><p/>
     * 分页获取系统用户登录日志列表
     * @apiGroup 1.基础数据
     * @apiParam {Long} adminId 管理员ID
     * @apiParam {String} [loginIp] 登录IP（支持模糊查询）
     * @apiParam {int} [pageNum=1] 页码
     * @apiParam {int} [pageSize=100] 每页条数
     * @apiSuccess {List(SysUserLoginLogEntity)} list 系统用户登录日志列表
     * @apiSuccess {Long} list.logId 登录日志ID
     * @apiSuccess {Long} list.loginTime 登录时间
     * @apiSuccess {String} list.loginIp 登录IP
     * @apiSuccess {Long} list.userId 用户ID
     * @apiSuccess {String} list.operatingSystem 操作系统
     * @apiSuccess {String} list.browser 浏览器
     * @apiSuccess {Page} page 分页信息
     * @apiSuccess {long} page.pageNum 当前页数
     * @apiSuccess {long} page.pages 总页数
     * @apiSuccess {long} page.total 总记录数
     * @apiSuccess {long} page.pageSize 每页记录数
     * @apiVersion 2.0.0
     * @apiSampleRequest /app
     * @apiParamExample {json} 请求示例
     * { "oper":"127.0.0.1", "appid":"1001", "random":"1234",
     * "service": "bootplus.user.login.log.list",
     * "adminId": 1
     * }
     * @apiSuccessExample {json} 响应示例
     * { "code":"0", "succ":true,
     * "list": [
     * {
     * "logId": 402,
     * "loginTime": 1528708116,
     * "loginIp": "61.142.209.194",
     * "userId": 1,
     * "operatingSystem": "WINDOWS_81",
     * "browser": "CHROME"
     * }
     * ],
     * "page": {
     * "pageNum": 1,
     * "pages": 18,
     * "total": 18,
     * "pageSize": 1
     * }
     * }
     */
    @ApiMethod(value = "bootplus.user.login.log.list", permission = {UserTypeConst.ADM, UserTypeConst.SYS}
            , logical = Logical.OR)
    public TransBaseResponse list(TransUserLoginRequest request) {
        // 执行校验
        TransBaseResponse response = Validate4Api.valid2Response(request, Lists.newArrayList(
                Validate4ApiRule.required("adminId", "管理员ID"),
                Validate4ApiRule.maxlength("loginIp", "登录IP", 10)
        ));
        if (null != response) {
            return response;
        }

        final Page<SysUserLoginLogEntity> page = sysUserLoginLogService.getPage(request.getPageNum(), request.getPageSize(),
                request.getAdminId(), request.getLoginIp(), null, null);

        TransPageBaseResponse pageResponse = new TransPageBaseResponse();
        pageResponse.setCode(ResponseCodeConst.SUCCESS);
        return pageResponse.flushPageList(PageUtils.buildPageUtil(page));
    }

}
