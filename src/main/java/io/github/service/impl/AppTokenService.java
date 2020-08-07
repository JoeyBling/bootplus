package io.github.service.impl;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.config.aop.service.BaseAopContext;
import io.github.frame.prj.constant.AppResponseCodeConst;
import io.github.frame.cache.annotation.MyCachePut;
import io.github.frame.cache.annotation.MyCacheable;
import io.github.frame.prj.model.TokenObject;
import io.github.util.exception.SysRuntimeException;
import io.github.util.http.CookieUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Token服务
 *
 * @author Created by 思伟 on 2020/8/4
 */
@Service
public class AppTokenService extends BaseAopContext<AppTokenService> {

    /**
     * 全局默认Cookie名
     */
    private static final String TOKEN_COOKIE_NAME = "_bootplus_token_";

    /**
     * 密钥(后续改成每个app独有信息)
     */
    static final String KEY_STR = "oOaB4X8SVm/79vZY9rHG0A==";

    /**
     * 登录
     *
     * @param appid       应用id
     * @param accessToken 授权accessToken
     * @param resp        HTTP响应
     * @return TokenObject
     */
    public TokenObject login(String appid, String accessToken, HttpServletResponse resp) {
        String json = SmUtil.sm4(Base64.decodeBase64(KEY_STR)).decryptStr(accessToken);
        TokenObject tokenObject;
        try {
            tokenObject = JSONObject.parseObject(json, TokenObject.class);
            tokenObject.setAppid(appid);
            // 缓存过期时间
            long expireTime = JSONObject.parseObject(json).getLongValue("expire");
            // Token名
            String tokenName = UUID.randomUUID().toString().replaceAll("-", "");

            // 放入缓存
            self.putToken(tokenName, tokenObject);

            // 将token存入cookie
            Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, tokenName);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            CookieUtil.addCookie(resp, cookie);
        } catch (Exception e) {
            throw new SysRuntimeException(AppResponseCodeConst.ERROR_VALIDATE, e.getMessage());
        }
        return tokenObject;
    }

    /**
     * 从http cookie中解析token
     *
     * @param request HTTP请求
     * @return TokenObject
     */
    public TokenObject parseTokenObject(HttpServletRequest request) {
        TokenObject tobj = null;
        String tokenName = null;
        Cookie cookie = CookieUtil.getCookie(request, TOKEN_COOKIE_NAME);
        if (null != cookie && StringUtils.isNotBlank(cookie.getValue())) {
            tokenName = cookie.getValue();
            tobj = self.getToken(tokenName);
        }
        logger.debug("set token={}, tokenObject={}", tokenName, JSON.toJSONString(tobj));
        return tobj;
    }

    /**
     * 缓存Token
     *
     * @param tokenName   Token名
     * @param tokenObject 用户请求Token
     * @return TokenObject
     */
    @MyCachePut(key = "#tokenName")
    public TokenObject putToken(String tokenName, TokenObject tokenObject) {
        return tokenObject;
    }

    /**
     * 从缓存获取Token
     *
     * @param tokenName Token名
     * @return TokenObject
     */
    @MyCacheable(key = "#tokenName")
    public TokenObject getToken(String tokenName) {
        return null;
    }

    public static void main(String[] args) {
        SymmetricCrypto sm4 = SmUtil.sm4(Base64.decodeBase64(KEY_STR));
        String admEncrypt = sm4.encryptHex("{\"userId\":\"1\",\"userType\":\"ADM\",\"expire\":1609430400}");
        System.out.println(admEncrypt);
        String sysEncrypt = sm4.encryptHex("{\"userId\":\"2\",\"userType\":\"SYS\",\"expire\":1609430400}");
        System.out.println(sysEncrypt);
    }

}
