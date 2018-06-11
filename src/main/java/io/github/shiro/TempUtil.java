package io.github.shiro;

import io.github.service.SysMenuService;
import io.github.service.SysUserService;
import io.github.util.Constant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Joey
 * @Configuration导致Shiro比Service实例化先执行
 * @Email 2434387555@qq.com
 */
@Component
public class TempUtil {

    public static SysMenuService sysMenuService;
    public static SysUserService sysUserService;
    public static Constant constant;

    @Resource
    public void setSysMenuService(SysMenuService sysMenuService) {
        TempUtil.sysMenuService = sysMenuService;
    }

    @Resource
    public void setSysUserService(SysUserService sysUserService) {
        TempUtil.sysUserService = sysUserService;
    }

    @Resource
    public void setConstant(Constant constant) {
        TempUtil.constant = constant;
    }

}
