package io.github.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.config.aop.service.BaseAopService;
import io.github.dao.SysUserLoginLogDao;
import io.github.entity.SysUserLoginLogEntity;
import io.github.service.SysUserLoginLogService;
import io.github.util.MethodNameUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户登录日志
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Service
public class SysUserLoginLogServiceImpl extends BaseAopService<SysUserLoginLogServiceImpl, SysUserLoginLogDao, SysUserLoginLogEntity>
        implements SysUserLoginLogService {

    @Override
    public Page<SysUserLoginLogEntity> getPage(Integer offset, Integer limit, Long adminId, String loginIp, String sort,
                                               Boolean isAsc) {
        QueryWrapper<SysUserLoginLogEntity> wrapper = new QueryWrapper();
        wrapper.lambda().eq(SysUserLoginLogEntity::getUserId, adminId);
        if (StringUtils.isNoneBlank(sort) && null != isAsc) {
            wrapper.orderBy(true, isAsc, MethodNameUtil.camel2underStr(sort));
        }
        if (StringUtils.isNoneBlank(loginIp)) {
            wrapper.lambda().like(SysUserLoginLogEntity::getLoginIp, loginIp);
        }
        Page<SysUserLoginLogEntity> page = new Page<SysUserLoginLogEntity>(offset, limit);
        return this.page(page, wrapper);
    }

}
