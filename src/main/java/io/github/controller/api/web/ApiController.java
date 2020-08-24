package io.github.controller.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.github.frame.log.LogUtil;
import io.github.frame.prj.constant.AppResponseCodeConst;
import io.github.frame.prj.handler.ParameterHandler;
import io.github.frame.prj.handler.TransferResponseHandler;
import io.github.frame.prj.transfer.request.TransBaseRequest;
import io.github.frame.prj.transfer.response.TransBaseResponse;
import io.github.frame.spring.context.handler.ApiServiceInitService;
import io.github.service.impl.AppConfigService;
import io.github.service.impl.AppTokenService;
import io.github.util.DateUtils;
import io.github.util.StringUtils;
import io.github.util.exception.RRException;
import io.github.util.exception.SysRuntimeException;
import io.github.util.http.GetIpAddress;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * API请求网关统一入口
 *
 * @author Updated by 思伟 on 2019/11/27
 */
@RestController
public class ApiController {
    private Logger logger = LogUtil.getInstance().getInterceptorStatementLogger().getLogger();

    @Resource
    private AppTokenService tokenService;
    @Resource
    private AppConfigService appConfigService;

    @RequestMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransBaseResponse app(HttpServletRequest request, HttpServletResponse response,
                                 TransBaseRequest transRequest, @RequestBody(required = false) String json) {
        long startTime = DateUtils.currentTimeStamp();
        TransBaseResponse transResponse = null;
        try {
            String sign = request.getHeader("sign");
            if (StringUtils.isNotBlank(json)) {
                transRequest = JSON.parseObject(json, TransBaseRequest.class);
                logger.debug("API请求访问[{}], 请求参数[{}]", transRequest.getService(), json);
            } else {
                logger.debug("API请求访问[{}], 请求参数[{}]", request.getParameter("service"),
                        toJsonString(request.getParameterMap()));
            }
            if (StringUtils.isNotBlank(sign)) {
                transRequest.setSign(sign);
            }

            transRequest.setTobj(tokenService.parseTokenObject(request));

            // 校验系统级参数
            TransferResponseHandler.validTransHeader(transRequest);

            // 设置ip地址
            String ip = transRequest.getIp();
            if (!GetIpAddress.isIPAddr(ip) || GetIpAddress.isLocalIPAddr(ip)) {
                transRequest.setIp(GetIpAddress.getIpAddress(request));
            }

            // 签名验证
            if (!appConfigService.validateSign(transRequest.getSign(), json, request.getParameterMap())) {
                // 数字签名校验错误
                throw new SysRuntimeException(AppResponseCodeConst.ERROR_SIGN);
            }

            // 接口定义校验
            if (!ApiServiceInitService.API_METHOD_MAP.containsKey(transRequest.getService())) {
                // 请求类型未定义
                throw new SysRuntimeException(AppResponseCodeConst.ERROR_SERVICE_NOT_FOUND);
            }

            // TODO：将appid、BillActor存入ThreadLocal
            // 获得业务处理对象
            ApiServiceInitService.ApiMethodHandler apiMethodHandler =
                    ApiServiceInitService.API_METHOD_MAP.get(transRequest.getService());

            Class<?>[] requestTypes = apiMethodHandler.getRequestTypes();

            Object[] objects = ParameterHandler.bindRequestParams(requestTypes, request,
                    response, transRequest, json);

            // 对string类型做js校验并替换
            // ParamsValidateUtil.filterScript(objects[0]);

            transResponse = (TransBaseResponse) apiMethodHandler.getHandlerMethod()
                    .invoke(apiMethodHandler.getHandler(), objects);

        } catch (InvocationTargetException e) {
            // 这里自定义异常需要捕获并处理
            if (SysRuntimeException.class.isAssignableFrom(e.getTargetException().getClass())) {
                SysRuntimeException runtimeException = (SysRuntimeException) e.getTargetException();
                transResponse = TransferResponseHandler.getErrResponse(runtimeException.getCode(), runtimeException.getMsg());
            } else if (RRException.class.isAssignableFrom(e.getTargetException().getClass())) {
                RRException rrException = (RRException) e.getTargetException();
                transResponse = TransferResponseHandler.getErrResponse(
                        StringUtils.toString(rrException.getCode()), rrException.getMsg());
            } else {
                logger.error("系统错误，" + e.getMessage(), e);
                // 请求非法
                transResponse = TransferResponseHandler.getErrResponse(AppResponseCodeConst.ERROR_ILLEGAL_REQUEST);
            }
        } catch (SysRuntimeException e) {
            transResponse = TransferResponseHandler.getErrResponse(e.getCode(), e.getMsg());
        } catch (JSONException e) {
            // 参数格式错误
            transResponse = TransferResponseHandler.getErrResponse(AppResponseCodeConst.ERROR_ILLEGAL_PARAMS);
        } catch (Throwable t) {
            logger.error("[API请求] 请求非法, e={}", t.getMessage(), t);
            // 请求非法
            transResponse = TransferResponseHandler.getErrResponse(AppResponseCodeConst.ERROR_ILLEGAL_REQUEST);
        } finally {
            // 这里可以对全局进行资源处理释放等...
        }
        // 返回结果
        return writeResponse(transResponse, transRequest, startTime);
    }

    /**
     * 处理返回结果
     *
     * @param response     响应对象
     * @param transRequest 请求对象
     * @param startTime    接口开始处理时间
     * @param <T>
     * @return T
     */
    protected <T extends TransBaseResponse> T writeResponse(T response, TransBaseRequest transRequest, long startTime) {
        if (!response.isSucc() && StringUtils.isEmpty(response.getMsg())) {
            response.setMsg(AppResponseCodeConst.errMsgMap.get(response.getCode()));
        }
        if (null != transRequest) {
            response.setClientStr(transRequest.getClientStr());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[API请求] 访问[{}], 响应数据[{}], 耗时[{}ms]",
                    transRequest.getService(), toJsonString(response), DateUtils.currentTimeStamp() - startTime);
        }
        return response;
    }

    /**
     * 转JSON
     *
     * @param map 任意类型
     * @return JSON
     */
    private String toJsonString(Object map) {
        return Optional.ofNullable(map).map(req ->
                String.class.equals(req.getClass()) ? req.toString() : JSON.toJSONString(req)).orElse(null);
    }

}
