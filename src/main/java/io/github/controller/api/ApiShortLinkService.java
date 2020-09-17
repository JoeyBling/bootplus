package io.github.controller.api;

import com.google.common.collect.Lists;
import io.github.controller.api.transfer.request.TransShortLinkRequest;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.frame.prj.enums.ShortLinkForwardModeEnum;
import io.github.frame.prj.model.ShortLinkVO;
import io.github.frame.prj.service.BaseApiService;
import io.github.frame.prj.service.annotation.ApiMethod;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.frame.prj.validate.Validate4Api;
import io.github.frame.prj.validate.Validate4ApiRule;
import io.github.service.IShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

/**
 * 短链接接口
 *
 * @author Created by 思伟 on 2020/9/11
 */
@Service
public class ApiShortLinkService extends BaseApiService {

    @Autowired
    private IShortLinkService shortLinkService;

    /**
     * just test
     */
    public static void main(String[] args) {
        String shortLink = "http://dev-recipe.hztywl.cn/api/recipe/sl/I7QnFzAr";
        System.out.println(shortLink.substring(shortLink.lastIndexOf("/") + 1));
        shortLink = "I7QnFzAr";
        System.out.println(shortLink.substring(shortLink.lastIndexOf("/") + 1));
    }

    /**
     * @api {POST} bootplus.short.link.new 03.生成短链接
     * @apiPermission 不需要token
     * @apiDescription 接口名：<code>bootplus.short.link.new</code><p/>
     * 生成一个新的短链接
     * @apiGroup 1.基础数据
     * @apiParam {String} target 原链接
     * @apiParam {String} [forwardMode=GHOST] 前进模式（GHOST=伪装、REDIRECT=重定向）
     * @apiSuccess {ShortLinkVO} obj 短链接信息
     * @apiSuccess {String} obj.shortLink 短链接
     * @apiSuccess {String} obj.target 原链接
     * @apiSuccess {String} obj.forwardMode 前进模式
     * @apiVersion 2.0.0
     * @apiSampleRequest /app
     * @apiParamExample {json} 请求示例
     * { "oper":"127.0.0.1", "appid":"1001", "random":"1234",
     * "service": "bootplus.short.link.new",
     * "target": "https://www.baidu.com/"
     * }
     * @apiSuccessExample {json} 响应示例
     * { "code":"0", "succ":true,
     * "obj": {
     * "forwardMode": "GHOST",
     * "shortLink": "http://bootplus.diandianys.com/sl/Fj7FyQ3y",
     * "target": "https://www.baidu.com/"
     * }
     * }
     */
    @ApiMethod(name = {"bootplus.short.link.new"})
    public TransBaseResponse generateShortLink(TransShortLinkRequest request) {
        // 执行校验
        TransBaseResponse responseErr = Validate4Api.valid2Response(request, Lists.newArrayList(
                Validate4ApiRule.required("target", "原链接"),
                Validate4ApiRule.enums("forwardMode", "前进模式",
                        Arrays.asList(ShortLinkForwardModeEnum.values()).stream()
                                .filter(modeEnum -> !ShortLinkForwardModeEnum.DEFAULT.equals(modeEnum))
                                .map(ShortLinkForwardModeEnum::name).toArray(String[]::new))
        ));
        if (null != responseErr) {
            return responseErr;
        }
        // 是否需要转码
        final String longUrl = request.getTarget();
        return TransBaseResponse.builder().code(ResponseCodeConst.SUCCESS)
                .obj(shortLinkService.generateShortLink(
                        Optional.ofNullable(request.getForwardMode()).map(ShortLinkForwardModeEnum::valueOf)
                                .orElse(ShortLinkForwardModeEnum.GHOST),
                        longUrl)).build();
    }

    /**
     * @api {POST} bootplus.short.link.get 04.获取短链接信息
     * @apiPermission 不需要token
     * @apiDescription 接口名：<code>bootplus.short.link.get</code><p/>
     * 获取短链接信息
     * @apiGroup 1.基础数据
     * @apiParam {String} shortLink 短链接
     * @apiSuccess {ShortLinkVO} obj 短链接信息
     * @apiSuccess {String} obj.shortLink 短链接
     * @apiSuccess {String} obj.target 原链接
     * @apiSuccess {String} [obj.forwardMode] 前进模式
     * @apiVersion 2.0.0
     * @apiSampleRequest /app
     * @apiParamExample {json} 请求示例
     * { "oper":"127.0.0.1", "appid":"1001", "random":"1234",
     * "service": "bootplus.short.link.get",
     * "shortLink": "http://bootplus.diandianys.com/sl/Fj7FyQ3y"
     * }
     * @apiSuccessExample {json} 响应示例
     * { "code":"0", "succ":true,
     * "obj": {
     * "forwardMode": "GHOST",
     * "shortLink": "http://bootplus.diandianys.com/sl/Fj7FyQ3y",
     * "target": "https://www.baidu.com/"
     * }
     * }
     */
    @ApiMethod(name = {"bootplus.short.link.get"})
    public TransBaseResponse getShortLink(TransShortLinkRequest request) {
        // 执行校验
        TransBaseResponse responseErr = Validate4Api.valid2Response(request, Lists.newArrayList(
                Validate4ApiRule.required("shortLink", "短链接")
        ));
        if (null != responseErr) {
            return responseErr;
        }
        // 有可能获取的短链接是空的，则返回请求信息
        final ShortLinkVO shortLink = Optional.ofNullable(
                shortLinkService.getShortLink(request.getShortLink().substring(request.getShortLink().lastIndexOf("/") + 1)))
                .orElse(ShortLinkVO.builder().shortLink(request.getShortLink()).target(request.getShortLink()).build());
        return TransBaseResponse.builder().code(ResponseCodeConst.SUCCESS)
                .obj(shortLink).build();
    }

}
