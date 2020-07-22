package io.github.frame.spring;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义拦截器接口
 *
 * @author Created by 思伟 on 2020/7/22
 */
public interface MyHandlerInterceptor extends HandlerInterceptor {

    /**
     * 拦截器默认排除路径(默认排除静态资源文件访问)
     */
    List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");

    /**
     * 获取注册的拦截器应用的URL模式
     *
     * @return String[]
     */
    default String[] getPath() {
        return new String[]{"/**"};
    }

    /**
     * 获取注册的拦截器排除的URL模式
     *
     * @return String[]
     */
    default String[] getExcludePath() {
        // 【强制】使用集合转数组的方法，必须使用集合的 toArray(T[] array)，传入的是类型完全一致、
        //长度为 0 的空数组。 等于 0，动态创建与 size 相同的数组，性能最好。
        return DEFAULT_EXCLUDE_PATH.toArray(new String[0]);
    }

}
