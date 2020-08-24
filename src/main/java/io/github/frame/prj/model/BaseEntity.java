package io.github.frame.prj.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
