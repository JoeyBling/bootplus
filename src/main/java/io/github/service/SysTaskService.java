package io.github.service;

import io.github.entity.SysTaskEntity;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.service.ISimpleService;

/**
 * 定时任务接口
 *
 * @author Created by 思伟 on 2020/8/21
 */
public interface SysTaskService extends ISimpleService<SysTaskEntity> {

    /**
     * 新增定时任务
     *
     * @param cronExpression cro表达式
     * @param jobName        任务名称，可以重复
     * @param bizModule      业务模块，取`SysModuleConst`值
     * @param bizId          业务id
     * @param bizTag         业务标识，当前模块内的业务标识，自定义，模块内不重复即可
     * @param callbackData   回调数据
     * @param callbackType   回调类型
     * @param callbackUrl    回调地址，print-不需要，class-注入spring的beanName，hessian、http、dubbo-具体地址
     * @return boolean
     */
    boolean addTask(String cronExpression, String jobName, String bizModule, String bizId, String bizTag,
                    String callbackData, TaskCallbackTypeEnum callbackType, String callbackUrl);

}
