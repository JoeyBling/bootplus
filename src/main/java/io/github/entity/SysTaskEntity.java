package io.github.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.model.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 平台_定时任务
 *
 * @author Created by 思伟 on 2020/8/21
 */
@Data
@NoArgsConstructor
@TableName("sys_task")
public class SysTaskEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 执行时间
     */
    @TableField
    private String cronExpression;

    /**
     * 任务名称
     */
    @TableField
    private String jobName;

    /**
     * 业务模块
     */
    @TableField
    private String bizModule;

    /**
     * 业务id
     */
    @TableField
    private String bizId;

    /**
     * 业务标识
     */
    @TableField
    private String bizTag;

    /**
     * 回调内容
     */
    @TableField
    private String callbackData;

    /**
     * 回调类型
     */
    @TableField
    private TaskCallbackTypeEnum callbackType;

    /**
     * 回调地址
     */
    @TableField
    private String callbackUrl;

    /**
     * 创建时间
     */
    @TableField
    private Date createTime;

    @Builder
    public SysTaskEntity(Long id, String cronExpression, String jobName, String bizModule, String bizId, String bizTag, String callbackData, TaskCallbackTypeEnum callbackType, String callbackUrl, Date createTime, Boolean enabled) {
        super(id,enabled);
        this.cronExpression = cronExpression;
        this.jobName = jobName;
        this.bizModule = bizModule;
        this.bizId = bizId;
        this.bizTag = bizTag;
        this.callbackData = callbackData;
        this.callbackType = callbackType;
        this.callbackUrl = callbackUrl;
        this.createTime = createTime;
    }

}
