package io.github.config.interceptor;

import io.github.frame.log.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

/**
 * 统一日志拦截器(也可以继承HandlerInterceptorAdapter)
 *
 * @author Created by 思伟 on 2019/12/25
 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter
 */
public class LogInterceptor implements HandlerInterceptor {

    /**
     * 拦截器日志
     */
    private final LogUtil.MyLogger log = LogUtil.getInstance().getInterceptorStatementLogger();

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss.SSS");

    /**
     * 默认本地执行线程开始时间标识
     */
    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new NamedThreadLocal<Long>("ThreadLocal_StartTime") {
        private Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        protected Long initialValue() {
            logger.warn("统一日志拦截器错误获取线程绑定变量，使用默认值...");
            return System.currentTimeMillis();
        }
    };

    /**
     * 在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理
     *
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 开始时间
        long beginTime = System.currentTimeMillis();
        // 线程绑定变量（该数据只有当前请求的线程可见）
        START_TIME_THREAD_LOCAL.set(beginTime);
        // log.debug("开始计时: {}  URI: {}", simpleDateFormat.format(beginTime), request.getRequestURI());
        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView
     *
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // log.info("ViewName: " + modelAndView.getViewName());
        }
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
     *
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (StringUtils.equalsAnyIgnoreCase(request.getRequestURI(),
                "/favicon.ico", "", "/")) {
            return;
        }
        try {
            // 打印JVM信息(可选保存日志操作等...)
            // 得到线程绑定的局部变量（开始时间）
            long beginTime = START_TIME_THREAD_LOCAL.get();
            // 结束时间
            long endTime = System.currentTimeMillis();
//                log.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
//                simpleDateFormat.format(endTime), (endTime - beginTime) / 1000 + "s", request.getRequestURI(),
//                        Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024,
//                        Runtime.getRuntime().freeMemory() / 1024 / 1024, (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024)
//                ;
        } finally {
            // 回收自定义的ThreadLocal变量
            START_TIME_THREAD_LOCAL.remove();
        }

    }

}