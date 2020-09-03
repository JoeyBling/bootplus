package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 系统用户
 * Bean Validation数据校验的认知：https://mp.weixin.qq.com/s/g04HMhrjbvbPn1Mb9JYa5g
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
    @NotBlank(message = "用户名不能为空")
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

    public static void main(String[] args) {
        // Bean Validation e.g.
        final SysUserEntity sysUserEntity = SysUserEntity.builder()
//                .username("Joey")
//                .sex(2)
                .status(true).build();
        // 1、使用【默认配置】得到一个校验工厂  这个配置可以来自于provider、SPI提供
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 2、得到一个校验器
        Validator validator = validatorFactory.getValidator();
        // 3、校验Java Bean（解析注解） 返回校验结果
        Set<ConstraintViolation<SysUserEntity>> result = validator.validate(sysUserEntity);
        // 输出校验结果
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);
    }

}
