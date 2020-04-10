package io.github.controller;

import com.alibaba.fastjson.JSON;
import io.github.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 错误控制器
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Slf4j
@Controller
public class MainSiteErrorController extends DefaultErrorAttributes implements ErrorController {

    public static final String ERROR_PATH = "/error";

    /**
     * Web页面错误处理
     */
    @RequestMapping(value = {ERROR_PATH}, produces = {MediaType.TEXT_HTML_VALUE})
    public String errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);
        log.error("errorHtml---errorAttributes={}", JSON.toJSONString(errorAttributes));
        return ERROR_PATH;
    }

    /**
     * 错误处理
     */
    @RequestMapping(value = {ERROR_PATH})
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public R handleError(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);
        log.error("handleError---errorAttributes={}", JSON.toJSONString(errorAttributes));
        Integer status = null != errorAttributes.get("status") ?
                Integer.valueOf(errorAttributes.get("status").toString()) : null;
        int code = null == status ? response.getStatus() : status;
        // 设置响应状态码
        response.setStatus(code);
        HttpStatus httpStatus = HttpStatus.valueOf(code);
        String httpCodeMsg = getHttpCodeMsg(httpStatus.value());
        if (StringUtils.isNoneEmpty(httpCodeMsg)) {
            return R.error(httpStatus.value(), httpCodeMsg);
        } else {
            return R.error();
        }
    }

    /**
     * 获取Http状态码描述
     *
     * @param code Http状态码
     */
    protected String getHttpCodeMsg(int code) {
        if (HttpStatus.NOT_FOUND.value() == code) {
            return "未找到资源";
        } else if (HttpStatus.FORBIDDEN.value() == code) {
            return "没有访问权限";
        } else if (HttpStatus.UNAUTHORIZED.value() == code) {
            return "登录过期";
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * 返回错误属性
     *
     * @param request           HttpServletRequest
     * @param includeStackTrace 包含堆栈跟踪元素
     * @return {@link Map}
     */
    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        /*return this.errorAttributes.getErrorAttributes(requestAttributes,
                includeStackTrace);*/
        return super.getErrorAttributes(requestAttributes,
                includeStackTrace);
    }

    /**
     * 错误属性
     */
    private ErrorAttributes errorAttributes;

    /*public MainSiteErrorController(@Autowired ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }*/

}
