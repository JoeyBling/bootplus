package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 角色
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@TableName("sys_role")
public class SysRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField
    private String roleName;

    /**
     * 备注
     */
    @TableField
    private String remark;

    /**
     * 创建时间
     */
    @TableField
    private Long createTime;

    @TableField(exist = false)
    private List<Long> menuIdList;

}
