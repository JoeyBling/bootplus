package io.github.frame.prj.handler;

import io.github.frame.prj.constant.AppResponseCodeConst;
import io.github.frame.prj.transfer.request.TransBaseRequest;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.frame.spring.context.handler.ApiServiceInitService;
import io.github.util.StringUtils;
import io.github.frame.prj.exception.SysRuntimeException;

/**
 * 接口响应处理实现
 *
 * @author Updated by 思伟 on 2019/11/27
 */
public class TransferResponseHandler {

    /**
     * 返回错误代码
     *
     * @param errorCode 错误代码
     * @return TransBaseResponse
     */
    public static TransBaseResponse getErrResponse(String errorCode) {
        TransBaseResponse baseResponse = new TransBaseResponse();
        baseResponse.setCode(errorCode);
        return baseResponse;
    }

    /**
     * 返回错误信息
     *
     * @param errorCode    错误代码
     * @param errorMessage 错误信息
     * @return TransBaseResponse
     */
    public static TransBaseResponse getErrResponse(String errorCode, String errorMessage) {
        TransBaseResponse baseResponse = new TransBaseResponse();
        baseResponse.setCode(errorCode);
        baseResponse.setMsg(errorMessage);
        return baseResponse;
    }

    /**
     * 校验请求参数
     *
     * @param request 请求对象
     */
    public static void validTransHeader(TransBaseRequest request) {
        if (request == null) {
            // 请求非法
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_ILLEGAL_REQUEST);
        }

        // 请求类型不能为空
        if (StringUtils.isEmpty(request.getService())) {
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_NEED_SERVICE);
        }

        // 服务商编号不能为空
        if (StringUtils.isEmpty(request.getAppid())) {
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_NEED_APPID);
        }

        // 数字签名不能为空
        if (StringUtils.isEmpty(request.getSign())) {
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_NEED_SIGN);
        }

        // 随机码不能为空
        if (StringUtils.isEmpty(request.getRandom())) {
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_NEED_RANDOM);
        }

        ApiServiceInitService.ApiMethodHandler handler =
                ApiServiceInitService.API_METHOD_MAP.get(request.getService());

        if (null == handler) {
            // 验证接口，如果接口不存在，返回无权调用
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_UNAUTHORIZED);
        }

        if (handler.getPermission().length > 0 && null == request.getTobj()) {
            // 请登录后再使用本功能
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_NEED_LOGIN);
        }

        if (handler.getPermission().length > 0 &&
                !StringUtils.equalsAny(request.getTobj().getUserType(), handler.getPermission())) {
            // 您的角色不允许进行此操作
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_UNAUTHORIZED);
        }

    }

}
