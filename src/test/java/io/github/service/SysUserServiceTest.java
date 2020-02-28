package io.github.service;

import io.github.entity.SysUserEntity;
import io.github.base.BaseAppTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户测试
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
public class SysUserServiceTest extends BaseAppTest {

    @Resource
    private SysUserService sysUserService;

    /**
     * 查询列表
     */
    //@Test
    public void selectList() {
        List<SysUserEntity> selectList = sysUserService.selectList(null);
        for (SysUserEntity sysUserEntity : selectList) {
            System.out.println(sysUserEntity);
        }
    }

}
