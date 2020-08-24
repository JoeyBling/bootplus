package io.github.task.quartz;

import io.github.config.aop.service.BaseAopContext;
import io.github.entity.SysTaskEntity;
import io.github.frame.prj.constant.SysModuleConst;
import io.github.frame.prj.util.QuartzUtil;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * 定时任务管理类
 *
 * @author Created by 思伟 on 2020/8/24
 */
@Service
public class QuartzManager extends BaseAopContext<QuartzManager> {
    /**
     * 调度器
     */
    @Resource
    private Scheduler scheduler;

    /**
     * 作业执行的任务对象
     */
    public static final String JOB_DATA_TASK_NAME = "BOOT_PLUS_JOB_DATA_TASK_NAME";

    /**
     * JobKey的group值
     */
    private static final String JOB_GROUP_NAME = "BOOT_PLUS_JOB_GROUP_NAME";

    /**
     * 添加一个定时任务
     *
     * @param jobName  任务名称
     * @param jobClass 要执行的作业的类
     * @param task     作业执行的任务对象
     */
    public void addJob(String jobName, Class jobClass, SysTaskEntity task) {
        Assert.notNull(task, "task must not be null");
        try {
            if (!task.getEnabled() || !QuartzUtil.canRun(task.getCronExpression())) {
                // 无效任务，或已过可执行时间，废弃任务
                return;
            }
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, JOB_GROUP_NAME)
                    .build();
            jobDetail.getJobDataMap().put(JOB_DATA_TASK_NAME, task);
            // 触发器
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, JOB_GROUP_NAME)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            logger.error("[{}] 添加任务失败，jobName={},e={}", SysModuleConst.SYS, jobName, e.getMessage(), e);
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName 任务名称
     */
    public void removeJob(String jobName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobName));
            if (null == jobDetail) {
                return;
            }
            // 停止触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
            // 移除触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName));
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName));
        } catch (Exception e) {
            logger.error("[{}] 移除任务失败，e={}", SysModuleConst.SYS, e.getMessage(), e);
        }
    }

    /**
     * 清理定时任务
     */
    public void clear() {
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            logger.error("[{}] 清理定时任务失败，e={}", SysModuleConst.SYS, e.getMessage(), e);
        }
    }

    /**
     * 启动定时任务
     */
    public void startJob() {
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (Exception e) {
            logger.error("[{}] 启动定时任务失败，e={}", SysModuleConst.SYS, e.getMessage(), e);
        }
    }

    /**
     * 关闭所有定时任务
     */
    public void shutdownJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            logger.error("[{}] 关闭定时任务失败，e={}", SysModuleConst.SYS, e.getMessage(), e);
        }
    }

}
