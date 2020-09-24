package io.github.base;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

/**
 * SpringBoot测试Shiro基类
 *
 * @author Created by 思伟 on 2020/9/21
 * @deprecated 授权失败
 */
@Deprecated
public abstract class BaseAppBootShiroTest extends BaseAppBootMvcTest {

    protected Subject subject;
    protected MockHttpServletRequest mockHttpServletRequest;
    protected MockHttpServletResponse mockHttpServletResponse;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockHttpServletRequest = new MockHttpServletRequest(webApplicationContext.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpServletRequest.setSession(mockHttpSession);

        // 防止Shiro报错
        SecurityManager securityManager = webApplicationContext
                .getBean(SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        // Shiro认证登陆
        login(getJunitEnv().getAdminName(), new Sha256Hash(getJunitEnv().getAdminPwd()).toHex());
        log.info("Shiro认证登陆成功");
    }

    private void login(String username, String password) {
        subject = new WebSubject.Builder(mockHttpServletRequest, mockHttpServletResponse)
                .buildWebSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
        subject.login(token);
        ThreadContext.bind(subject);
    }

}
