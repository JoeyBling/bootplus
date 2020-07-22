package io.github.frame.spring;

import io.github.util.ClassUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;

/**
 * 自定义拦截器接口
 *
 * @author Created by 思伟 on 2020/7/22
 */
public abstract class MyHandlerInterceptorAdapter extends HandlerInterceptorAdapter implements MyHandlerInterceptor {

    @Override
    public String toString() {
        return String.format("%s{path=%s, excludePath=%s}", ClassUtil.getClassName(this.getClass()),
                Arrays.toString(getPath()), Arrays.toString(getExcludePath()));
    }

}
