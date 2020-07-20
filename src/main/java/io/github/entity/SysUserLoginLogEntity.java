package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户登录日志
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_user_login_log")
public class SysUserLoginLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 登录日志ID
     */
    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * 登录时间
     */
    @TableField
    private Long loginTime;

    /**
     * 登录IP
     */
    @TableField
    private String loginIp;

    /**
     * 用户ID
     */
    @TableField
    private Long userId;

    /**
     * 操作系统
     */
    @TableField
    private String operatingSystem;

    /**
     * 浏览器
     */
    @TableField
    private String browser;

}
