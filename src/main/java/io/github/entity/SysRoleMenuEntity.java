package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@TableName("sys_role_menu")
public class SysRoleMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @TableField
    private Long roleId;

    /**
     * 菜单ID
     */
    @TableField
    private Long menuId;
}
