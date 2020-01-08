package io.github.config.filter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS过滤处理
 *
 * @author Created by 思伟 on 2019/12/25
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 唯一构造器
     *
     * @param request HttpServletRequest
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                if (null != values[i]) {
                    // 防xss攻击和过滤前后空格
                    escapeValues[i] = StringUtils.trim(Jsoup.clean(values[i], Whitelist.relaxed()));
                } else {
                    escapeValues[i] = null;
                }
            }
            return escapeValues;
        }
        return super.getParameterValues(name);
    }
}