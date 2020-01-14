package io.github.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.github.config.aop.BaseAopService;
import io.github.dao.SysUserDao;
import io.github.entity.SysUserEntity;
import io.github.service.SysUserRoleService;
import io.github.service.SysUserService;
import io.github.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@Service
@Slf4j
public class SysUserServiceImpl extends BaseAopService<SysUserServiceImpl, SysUserDao, SysUserEntity>
        implements SysUserService {

    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysUserDao dao;

    @Override
    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserEntity user) throws Exception {
//        log.debug("是否是代理调用:{}", AopUtils.isAopProxy(self));
        user.setCreateTime(DateUtils.getCurrentUnixTime());
        // sha256加密
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        baseMapper.insert(user);
        // 保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] userId) {
        baseMapper.deleteBatch(userId);
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return baseMapper.updatePassword(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserEntity entity) {
        if (StringUtils.isBlank(entity.getPassword())) {
            entity.setPassword(null);
        } else {

            entity.setPassword(new Sha256Hash(entity.getPassword()).toHex());
        }
        dao.updateUser(entity);
        // 保存用户与角色关系
        sysUserRoleService.saveOrUpdate(entity.getUserId(), entity.getRoleIdList());
    }

    @Override
    public Page<SysUserEntity> queryListByPage(Integer offset, Integer limit, String email, String userName,
                                               String sort, Boolean order) {
        Wrapper<SysUserEntity> wrapper = new EntityWrapper<SysUserEntity>();
        if (StringUtils.isNoneBlank(sort) && null != order) {
            wrapper.orderBy(sort, order);
        }
        if (StringUtils.isNoneBlank(userName)) {
            wrapper.like("username", userName);
        }
        if (StringUtils.isNoneBlank(email)) {
            wrapper.like("email", email);
        }
        Page<SysUserEntity> page = new Page<>(offset, limit);
        return this.selectPage(page, wrapper);
    }

    @Override
    public int updateAvatar(SysUserEntity entity) {
        int update = dao.updateAvatar(entity);
        return update;
    }

    @Override
    public int updateStatus(Long userId, int status) {
        return dao.updateStatus(userId, status);
    }

}
