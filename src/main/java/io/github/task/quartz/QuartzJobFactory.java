package io.github.task.quartz;

import com.alibaba.fastjson.JSON;
import io.github.entity.SysTaskEntity;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.constant.SysModuleConst;
import io.github.service.SysTaskService;
import io.github.util.spring.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * quartz任务调度工厂
 *
 * @author Created by 思伟 on 2020/8/24
 */
@Slf4j
public class QuartzJobFactory implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long start = System.currentTimeMillis();
        boolean result = false;
        // 从context中取出对应的【定时任务】
        SysTaskEntity task = (SysTaskEntity) context.getJobDetail().getJobDataMap().get(QuartzManager.JOB_DATA_TASK_NAME);
        switch (task.getCallbackType()) {
            case PRINT:
                result = printExecute(task);
                break;
            case CLASS:
                result = classExecute(task);
                break;
            case HTTP:
                // TODO...
                printLog(task);
                break;
            case HESSIAN:
                printLog(task);
                // TODO...
                break;
            default:
                result = false;
                break;
        }
        long time = System.currentTimeMillis() - start;
        log.info("[{}] 执行[{}]定时任务 {}，耗时={}ms, task={}", SysModuleConst.SYS,
                task.getCallbackType().getValue(), result ? "成功" : "失败", time, JSON.toJSONString(task));
        // 执行后就移除
        SpringContextUtils.getBean(SysTaskService.class).disableByPrimaryKey(task.getId());
    }

    /**
     * 普通打印任务
     *
     * @param task 定时任务
     * @return boolean
     * @see TaskCallbackTypeEnum#PRINT
     */
    private boolean printExecute(SysTaskEntity task) {
        printLog(task);
        return true;
    }

    /**
     * 类回调任务
     *
     * @param task 定时任务
     * @return boolean
     * @see TaskCallbackTypeEnum#CLASS
     */
    private boolean classExecute(SysTaskEntity task) {
        // TODO 不建议每次取，建议使用静态变量
        return SpringContextUtils.getBean(ITaskCallbackService.class).execute(
                task.getId(), task.getJobName(), task.getBizModule(), task.getBizId(), task.getBizTag(),
                task.getCallbackData(), task.getCallbackUrl());
    }

    /**
     * 简易日志打印
     *
     * @param task 定时任务
     */
    protected void printLog(SysTaskEntity task) {
        log.info("[{}] 执行[{}]定时任务，task={}", SysModuleConst.SYS, task.getCallbackType().getValue(),
                JSON.toJSONString(task));
    }
}
