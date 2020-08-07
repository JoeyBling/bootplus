package io.github.frame.prj.handler;

import com.alibaba.fastjson.JSON;
import io.github.controller.api.transfer.request.TransAppBaseRequest;
import io.github.frame.prj.constant.UserTypeConst;
import io.github.frame.prj.model.TokenObject;
import io.github.frame.prj.transfer.request.TransBaseRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 参数处理实现
 *
 * @author Updated by 思伟 on 2019/11/27
 */
public class ParameterHandler {

    /**
     * 绑定请求参数
     *
     * @param requestTypes Class
     * @param request      HTTP请求
     * @param response     HTTP响应
     * @param transRequest 请求对象
     * @param json         请求JSON
     * @return Object[]
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object[] bindRequestParams(Class<?>[] requestTypes, HttpServletRequest request,
                                             HttpServletResponse response, TransBaseRequest transRequest,
                                             String json) throws InstantiationException, IllegalAccessException {
        Object[] objects = new Object[requestTypes.length];

        for (int i = 0; i < objects.length; i++) {

            Class<?> requestType = requestTypes[i];

            Object requestObject = null;
            if (TransBaseRequest.class.isAssignableFrom(requestType)) {
                requestObject = requestTypes[i].newInstance();
                if (StringUtils.isBlank(json)) {
                    ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(requestObject);
                    dataBinder.bind(request);

                    // 绑定form-data参数
                    BeanUtils.copyProperties(transRequest, requestObject);
                    transRequest = (TransBaseRequest) requestObject;

                } else {
                    requestObject = JSON.parseObject(json, requestObject.getClass());
                    BeanUtils.copyProperties(transRequest, requestObject);
                    transRequest = (TransBaseRequest) requestObject;
                }

                if (TransAppBaseRequest.class.isAssignableFrom(requestObject.getClass())) {
                    // 设置默认渠道
                    TransAppBaseRequest appRequest = (TransAppBaseRequest) requestObject;
                    if (StringUtils.isEmpty(appRequest.getChannel())) {
                        appRequest.setChannel("0");
                    }
                    // 更具tokenObject设置docId和patId
                    TokenObject tobj = appRequest.getTobj();
                    if (null != tobj) {
                        appRequest.setUserId(UserTypeConst.APP.equals(tobj.getUserType()) && null != tobj.getUserId()
                                ? tobj.getUserId() : appRequest.getUserId());
                    }
                }
            } else if (requestType == HttpServletRequest.class) {
                requestObject = request;
            } else if (requestType == HttpServletResponse.class) {
                requestObject = response;
            }
            objects[i] = requestObject;
        }
        return objects;
    }

}
