package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_role")
public class SysUserRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField
    private Long userId;

    /**
     * 角色ID
     */
    @TableField
    private Long roleId;

}
