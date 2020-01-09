package io.github.util.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 *
 * @author Created by 思伟 on 2020/1/8
 */
public class CookieUtil {

    /**
     * 设置HttpOnly Cookie
     *
     * @param response   HTTP响应
     * @param cookie     Cookie对象
     * @param isHttpOnly 是否为HttpOnly
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie, boolean isHttpOnly) {
        //Cookie名称
        String name = cookie.getName();
        //Cookie值
        String value = cookie.getValue();
        //最大生存时间(毫秒,0代表删除,-1代表与浏览器会话一致)
        int maxAge = cookie.getMaxAge();
        //路径
        String path = cookie.getPath();
        //域
        String domain = cookie.getDomain();
        //是否为安全协议信息
        boolean isSecure = cookie.getSecure();

        StringBuilder buffer = new StringBuilder();

        buffer.append(name).append("=").append(value).append(";");

        if (maxAge == 0) {
            buffer.append("Expires=Thu Jan 01 08:00:00 CST 1970;");
        } else if (maxAge > 0) {
            buffer.append("Max-Age=").append(maxAge).append(";");
        }

        if (domain != null) {
            buffer.append("domain=").append(domain).append(";");
        }

        if (path != null) {
            buffer.append("path=").append(path).append(";");
        }

        if (isSecure) {
            buffer.append("secure;");
        }

        if (isHttpOnly) {
            buffer.append("HTTPOnly;");
        }
        response.addHeader("Set-Cookie", buffer.toString());
    }

}