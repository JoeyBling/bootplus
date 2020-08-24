package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 系统用户
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@TableName("sys_user")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @TableField
    private transient String password;

    /**
     * 性别 0=保密/1=男/2=女
     */
    @TableField
    @NotNull(message = "性别不能为空")
    private Integer sex;

    /**
     * 邮箱
     */
    @TableField
    private String email;

    /**
     * 手机号
     */
    @TableField
    private String mobile;

    /**
     * 最后登录时间
     */
    @TableField
    private Long lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField
    private String lastLoginIp;

    /**
     * 头像缩略图地址
     */
    private String avatarUrl;

    /**
     * 状态 0：禁用 1：正常
     */
    @TableField
    @NotNull(message = "状态不能为空")
    private Boolean status;

    /**
     * 创建时间
     */
    @TableField
    private Long createTime;

    /**
     * 角色ID列表(排除表字段)
     */
    @TableField(exist = false)
    private List<Long> roleIdList;

    @Builder
    public SysUserEntity(Long userId, String username, String password, Integer sex, String email, String mobile, Long lastLoginTime, String lastLoginIp, String avatarUrl, Boolean status, Long createTime, List<Long> roleIdList) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.email = email;
        this.mobile = mobile;
        this.lastLoginTime = lastLoginTime;
        this.lastLoginIp = lastLoginIp;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.createTime = createTime;
        this.roleIdList = roleIdList;
    }
}
