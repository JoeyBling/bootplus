package io.github.config;

import io.github.common.hessian.HessianServiceExporter;
import io.github.service.SysUserLoginLogService;
import io.github.service.hessian.IExtShortLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hessian接口配置
 *
 * @author Updated by 思伟 on 2020/6/28
 * @see HessianServiceExporter
 */
@Configuration
public class HessianInterfaceConfiguration {

    /**
     * 用户登录日志Hessian接口
     */
    @Bean(name = "/ExtSysUserLoginLogService")
    @ConditionalOnBean({SysUserLoginLogService.class})
    public HessianServiceExporter extRecipeOrderService(SysUserLoginLogService sysUserLoginLogService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(SysUserLoginLogService.class);
        exporter.setService(sysUserLoginLogService);
        return exporter;
    }

    /**
     * 短链接生成Hessian接口
     * 不能通过方法名来指定URL即可，必须为/开头
     */
    @Bean("/IExtShortLinkService")
    @ConditionalOnBean({IExtShortLinkService.class})
    public HessianServiceExporter iExtShortLinkService(IExtShortLinkService shortLinkService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(IExtShortLinkService.class);
        exporter.setService(shortLinkService);
        return exporter;
    }

}
