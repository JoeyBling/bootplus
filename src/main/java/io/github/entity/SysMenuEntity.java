package io.github.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.entity.enums.SysMenuTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Data
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    @TableField
    private Long parentId;

    /**
     * 菜单名称
     */
    @TableField
    private String name;

    /**
     * 菜单URL
     */
    @TableField
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @TableField
    private String perms;

    /**
     * 类型：目录、菜单、按钮
     */
    @TableField
    private SysMenuTypeEnum type;

    /**
     * 菜单图标
     */
    @TableField
    private String icon;

    /**
     * 排序
     */
    @TableField
    private Integer orderNum;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    @Transient
    private String parentName;

    /**
     * ztree属性
     */
    @TableField(exist = false)
    @Transient
    private Boolean open;

    @TableField(exist = false)
    @Transient
    private List<?> list;

}
