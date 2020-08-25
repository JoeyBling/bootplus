package io.github.frame.prj.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * 基础实体类
 *
 * @author Created by 思伟 on 2020/8/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements Entity {

    /**
     * ID主键
     */
    @Id
    @TableId(type = IdType.INPUT)
    /**
     * 解决Jackson导致Long型数据精度丢失问题
     * 是否考虑直接使用String作为ID类型
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 是否有效
     */
    @TableField
    private Boolean enabled;

    @Override
    public String getModule() {
        return null;
    }

    @Override
    public boolean isNew() {
        return null == id;
    }
}
