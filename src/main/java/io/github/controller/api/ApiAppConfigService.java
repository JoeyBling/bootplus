package io.github.controller.api;

import com.google.common.collect.Lists;
import io.github.controller.api.transfer.request.TransAppLoginRequest;
import io.github.frame.prj.service.annotation.ApiMethod;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.frame.prj.validate.Validate4Api;
import io.github.frame.prj.validate.Validate4ApiRule;
import io.github.frame.prj.service.ApiService;
import io.github.service.impl.AppConfigService;
import io.github.service.impl.AppTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 应用接口
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Service
public class ApiAppConfigService implements ApiService {

    @Resource
    private AppConfigService appConfigService;

    @Resource
    private AppTokenService appTokenService;

    /**
     * @api {POST} bootplus.user.login 01.用户登录
     * @apiPermission 不需要token
     * @apiDescription 接口名：<code>bootplus.user.login</code><p/>
     * 用户登录，用传入的accessToken完成用户登录，并返回当前用户信息<p/>
     * **ADM test accessToken:**
     * 61466acb7a15d4ca2dbaacfa45fc7eb2c9af92c0f3b72aaa8d885b3b26e8c45757abd97fab3b2d996fce5c66050c0bd3dd432c6b4c603c18244798c36af22acf
     * **SYS test accessToken:**
     * 2da802d5495019986a2378eb3dd6cd95af6f1d3d94d3d9f1524c414ce50e246c57abd97fab3b2d996fce5c66050c0bd3dd432c6b4c603c18244798c36af22acf
     * @apiGroup 1.基础数据
     * @apiParam {String} accessToken 授权accessToken
     * @apiSuccess {Object(TokenObject)} obj 用户token
     * @apiSuccess {String}  obj.userId 用户id
     * @apiSuccess {String}  obj.userType 用户类型
     * @apiSuccess {String}  obj.appid 用户所属机构
     * @apiVersion 2.0.0
     * @apiSampleRequest /app
     * @apiParamExample {json} 请求示例
     * { "oper":"127.0.0.1", "appid":"1001", "random":"1234",
     * "service": "bootplus.user.login", "accessToken": "61466acb7a15d4ca2dbaacfa45fc7eb2c9af92c0f3b72aaa8d885b3b26e8c45757abd97fab3b2d996fce5c66050c0bd3dd432c6b4c603c18244798c36af22acf"
     * }
     * @apiSuccessExample {json} 响应示例
     * { "code":"0", "succ":true,
     * "obj":{"appid":"1001", "userId":"1", "userType":"ADM"}
     * }
     */
    @ApiMethod(name = "bootplus.user.login")
    public TransBaseResponse login(TransAppLoginRequest request, HttpServletResponse resp) {
        // 执行校验
        TransBaseResponse response = Validate4Api.valid2Response(request, Lists.newArrayList(
                Validate4ApiRule.required("accessToken", "accessToken")
        ));
        if (null != response) {
            return response;
        }

        return TransBaseResponse.builder()
                .code(ResponseCodeConst.SUCCESS)
                .obj(appTokenService.login(request.getAppid(), request.getAccessToken(), resp)).build();
    }

}
