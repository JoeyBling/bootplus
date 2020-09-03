package io.github.frame.prj.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.github.frame.prj.constant.ResponseCodeConst;
import io.github.util.exception.SysRuntimeException;
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
    @JSONField(serialize = false)
    @JsonIgnore
    public String getModule() {
        throw new SysRuntimeException(ResponseCodeConst.ERROR_VALIDATE, "谁没有在model中重写getModule方法");
//        return null;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isNew() {
        // if ID type==String using StringUtils.isBlank
        return null == id;
    }
}
