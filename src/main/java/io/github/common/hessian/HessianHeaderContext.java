package io.github.common.hessian;

import io.github.frame.constant.SystemConst;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Hessian协议请求头上下文
 *
 * @author Created by 思伟 on 2020/7/27
 */
@Slf4j
public class HessianHeaderContext {
    /**
     * 默认编码
     */
    private static final Charset DEFAULT_CHARSET = SystemConst.DEFAULT_CHARSET;

    /**
     * 默认本地执行线程请求头上下文
     */
    private static final ThreadLocal<HessianHeaderContext> THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new HessianHeaderContext());

    public static HessianHeaderContext setContext(Boolean encode) {
        THREAD_LOCAL.set(new HessianHeaderContext(Boolean.TRUE.equals(encode)));
        return getContext();
    }

    /**
     * 获取请求头信息
     */
    public static HessianHeaderContext getContext() {
        return THREAD_LOCAL.get();
    }

    /**
     * 清除本地线程变量
     */
    public static void clear() {
        synchronized (THREAD_LOCAL) {
            HessianHeaderContext context = THREAD_LOCAL.get();
            if (context != null) {
                context.headers.clear();
                THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * 请求头信息
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 外部不提供实例化方法
     */
    private HessianHeaderContext() {
        this(false);
    }

    private HessianHeaderContext(boolean encode) {
        this.encode = encode;
    }

    /**
     * 添加头信息
     *
     * @param name  键
     * @param value 值
     * @return self
     */
    public HessianHeaderContext addHeader(String name, String value) {
        headers.put(name, URLEncode(value));
        return this;
    }

    /**
     * 获取指定键的值
     *
     * @param name 键
     * @return 值
     */
    public String getHeader(String name) {
        return URLDecode(headers.get(name));
    }

    /**
     * 获取请求头信息
     *
     * @return Map
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 是否对数据值进行URL编码和解码（防止中文乱码）
     */
    private boolean encode = false;

    /**
     * URL编码
     *
     * @param value 值
     * @return String
     * @see #encode
     */
    protected String URLEncode(String value) {
        if (Boolean.TRUE.equals(this.encode)) {
            try {
                return URLEncoder.encode(value, DEFAULT_CHARSET.name());
            } catch (UnsupportedEncodingException e) {
                // encode error
                log.error("URLEncoder.encode error:{},use original value:[{}]", e.getMessage(), value);
            }
        }
        return value;
    }

    /**
     * URL解码
     *
     * @param value 值
     * @return String
     * @see #encode
     */
    protected String URLDecode(String value) {
        if (Boolean.TRUE.equals(this.encode)) {
            try {
                return URLDecoder.decode(value, DEFAULT_CHARSET.name());
            } catch (UnsupportedEncodingException e) {
                // decode error
                log.error("URLEncoder.decode error:{},use original value:[{}]", e.getMessage(), value);
                return value;
            }
        }
        return value;
    }

}