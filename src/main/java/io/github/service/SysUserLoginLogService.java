package io.github.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.entity.SysUserLoginLogEntity;

/**
 * 用户登录日志
 *
 * @author Created by 思伟 on 2020/6/6
 */
public interface SysUserLoginLogService extends IService<SysUserLoginLogEntity> {

    /**
     * 根据管理员ID获取登录日志
     *
     * @param offset  开始
     * @param limit   条数
     * @param adminId 管理员ID
     * @param loginIp 登录IP(筛选模糊查询)
     * @param sort    排序字段
     * @param order   是否为升序
     * @return Page<SysUserLoginLogEntity>
     */
    Page<SysUserLoginLogEntity> getPage(Integer offset, Integer limit, Long adminId, String loginIp, String sort,
                                        Boolean order);

}
