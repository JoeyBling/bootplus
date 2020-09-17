package io.github.util.http;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import io.github.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest帮助类
 *
 * @author Created by 思伟 on 2020/6/6
 */
public class HttpUtil {

    /**
     * 获取当前用户浏览器信息
     *
     * @param request HttpServletRequest
     * @return 当前用户浏览器信息
     */
    public static String getHeader(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 获取当前用户浏览器型号
     *
     * @param request HttpServletRequest
     * @return 当前用户浏览器型号
     */
    public static String getUserBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.toString();
    }

    /**
     * 获取当前用户系统型号
     *
     * @param request HttpServletRequest
     * @return 当前用户系统型号
     */
    public static String getUserOperatingSystem(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.toString();
    }

    /**
     * 拼接URL字符串(同时会转换\或/出现2次以上的转/)推荐使用
     *
     * @param url URL字符串
     * @return 拼接后的URL字符串
     */
    public static final String generateHttpUrl(final String... url) {
        if (null == url) {
            return null;
        }
//        org.apache.commons.lang3.StringUtils.trimToEmpty(url);
        //ArrayUtils.nullToEmpty(url);

        // StringUtils.join自动过滤Null值
        String uri = StringUtils.join(url, "/");
        // (?i)在前面 不区分大小写
        // ((ht|f)tp(s?)\:)?
        return uri.replaceAll("(\\\\|/){2,}", "/")
                .replaceFirst("(?i)((ht|f)tp\\:(\\\\|/)+)", "http://")
                .replaceFirst("(?i)((ht|f)tps\\:(\\\\|/)+)", "https://");
    }

    public static void main(String[] args) {
        String s = generateHttpUrl("www.baidu,com\\\\sad//asdas",
                "https://asd\\", "啊是大", "////s///adada", null);
        System.out.println(s);
    }

}
