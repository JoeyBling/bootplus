package io.github.config;

import io.github.shiro.UserRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Configuration
@Slf4j
public class ShiroConfiguration {

    /**
     * 是否在https中才有效【只能用https协议发送给服务器】
     */
    @Value("${shiro.cookie-secure:false}")
    private boolean cookieSecure;

    /**
     * 更安全，防止XSS攻击
     */
    @Value("${shiro.cookie-http-only:true}")
    private boolean cookieHttpOnly;

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        log.info("ShiroConfiguration.shiroFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/admin/sys/logout", "logout");

        // 配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/admin/index", "user");

        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        // noSessionCreation
        filterChainDefinitionMap.put("/statics/**", "anon");
        filterChainDefinitionMap.put("/admin/captcha.jpg", "anon");
        filterChainDefinitionMap.put("/share/qrcode", "anon");
        filterChainDefinitionMap.put("/admin/sys/login", "anon");
        filterChainDefinitionMap.put("/admin/sys/logout", "anon");
        filterChainDefinitionMap.put("/admin/**", "authc");

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/admin/login.html");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/admin/");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/error.html");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(UserRealm userRealm, EhCacheManager ehCacheManager,
                                           CookieRememberMeManager rememberMeManager,
                                           SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(userRealm);

        // 注入缓存管理器;  这个如果执行多次，也是同样的一个对象;
        securityManager.setCacheManager(ehCacheManager);

        // 注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager);

        // session管理器
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * session管理器
     */
    @Bean
    public SessionManager getDefaultWebSessionManager(SessionDAO sessionDAO, @Qualifier("shareSessionCookie") Cookie cookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 会话超时时间，单位：毫秒  20m=1200000ms, 30m=1800000ms, 60m=3600000ms
        sessionManager.setGlobalSessionTimeout(60 * 30 * 1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(cookie);
        // URL重写中去掉 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
//        List<SessionListener> listeners = new ArrayList<SessionListener>();
//        listeners.add(getUserSessionListener());
//        sessionManager.setSessionListeners(listeners);
        return sessionManager;
    }

    /**
     * cookie对象
     */
    @Bean("shareSessionCookie")
    public SimpleCookie shareSessionCookie() {
        log.info("ShiroConfiguration.shareSessionCookie()");
        // cookie的name,对应的默认是 JSESSIONID
        SimpleCookie simpleCookie = new SimpleCookie("SHAREJSESSIONID");
        //jsessionId的path为 / 用于多个系统共享jsessionId
        simpleCookie.setPath("/");
        // <!-- cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        // more secure, protects against XSS attacks
        simpleCookie.setHttpOnly(cookieHttpOnly);
        simpleCookie.setSecure(cookieSecure);
        return simpleCookie;
    }

    /**
     * session存储的实现
     */
    @Bean
    public SessionDAO sessionDAO() {
        return new EnterpriseCacheSessionDAO();
    }

    /**
     * 身份认证realm(账号密码校验，权限等)
     */
    @Bean
    public UserRealm getUserRealm() {
        return new UserRealm();
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * shiro缓存管理器; 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * 可见securityManager是整个shiro的核心
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        log.info("ShiroConfiguration.getEhCacheManager()");
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

    /**
     * cookie对象
     */
    @Bean("rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        log.info("ShiroConfiguration.rememberMeCookie()");
        // 这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
        // cookie的name,对应的默认是 JSESSIONID
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //jsessionId的path为 / 用于多个系统共享jsessionId
        simpleCookie.setPath("/");
        // <!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        // more secure, protects against XSS attacks
        simpleCookie.setHttpOnly(cookieHttpOnly);
        simpleCookie.setSecure(cookieSecure);
        return simpleCookie;
    }

    /**
     * cookie管理对象
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(@Qualifier("rememberMeCookie") SimpleCookie cookie) {
        log.info("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
        cookieRememberMeManager.setCipherKey(cipherKey);
        return cookieRememberMeManager;
    }

}
