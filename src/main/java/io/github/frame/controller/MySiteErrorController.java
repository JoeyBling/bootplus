package io.github.frame.controller;

import com.alibaba.fastjson.JSON;
import io.github.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 自定义错误控制器
 * https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-error-handling
 *
 * @author Updated by 思伟 on 2020/7/10
 * @see io.github.config.MyBootConfig.MyErrorAttributes
 */
@Slf4j
@Controller
//@RequestMapping("${server.error.path:${error.path:/error}}")
public class MySiteErrorController extends BasicErrorController {

    /**
     * Web页面错误处理
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {

        /*for (java.util.Enumeration<String> en = request.getAttributeNames(); en.hasMoreElements(); ) {
            String attribute = en.nextElement();
            System.out.println(attribute);
        }*/

        Map<String, Object> errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        log.error("errorHtml---errorAttributes={}", JSON.toJSONString(errorAttributes));
        /**
         * @see ErrorMvcAutoConfiguration.WhitelabelErrorViewConfiguration
         * @see ErrorMvcAutoConfiguration.StaticView#render
         * @see TemplateAvailabilityProviders#getProvider
         */
        return super.errorHtml(request, response);
    }

    /**
     * 错误处理
     */
    @Override
//    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        log.error("handleError---errorAttributes={}", JSON.toJSONString(errorAttributes));
        HttpStatus httpStatus = getStatus(request);
        if (httpStatus == HttpStatus.NO_CONTENT) {
            return ResponseEntity.noContent().build();
        }
        String httpCodeMsg = getHttpCodeMsg(httpStatus.value());
        ResponseEntity<Map<String, Object>> body;
        if (StringUtils.isNoneEmpty(httpCodeMsg)) {
            body = ResponseEntity.status(httpStatus)
                    .body(R.error(httpStatus.value(), httpCodeMsg).myPutAll(errorAttributes));
        } else {
            body = ResponseEntity.status(httpStatus).body(R.error().myPutAll(errorAttributes));
        }
        return body;
    }

    /**
     * 当请求处理程序无法生成客户端可接受的响应时引发异常
     *
     * @see HttpMediaTypeNotAcceptableException
     */
    @Override
    public ResponseEntity<String> mediaTypeNotAcceptable(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        log.error("mediaTypeNotAcceptable---errorAttributes={}", JSON.toJSONString(errorAttributes));
        return super.mediaTypeNotAcceptable(request);
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
            HttpStatus httpStatus = null;
            try {
                httpStatus = HttpStatus.valueOf(code);
            } catch (Exception e) {
            }
            // getReasonPhrase()
            return null != httpStatus ? httpStatus.toString() : StringUtils.EMPTY;
        }
    }

   /* public MySiteErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        this(errorAttributes, serverProperties, null);
    }*/

    /**
     * @see ErrorMvcAutoConfiguration#basicErrorController(org.springframework.boot.web.servlet.error.ErrorAttributes, org.springframework.beans.factory.ObjectProvider)
     */
    public MySiteErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                 ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), Optional.ofNullable(errorViewResolvers.orderedStream().collect(Collectors.toList())).orElse(Collections.emptyList()));
    }

}
