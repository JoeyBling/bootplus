package io.github.service.impl;

import io.github.dao.SysTaskDao;
import io.github.entity.SysTaskEntity;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.constant.SysModuleConst;
import io.github.frame.prj.service.SimpleService;
import io.github.frame.spring.IStartUp;
import io.github.service.SysTaskService;
import io.github.task.quartz.QuartzJobFactory;
import io.github.task.quartz.QuartzManager;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 定时任务实现
 *
 * @author Created by 思伟 on 2020/8/21
 */
@Service
public class SysTaskServiceImpl extends SimpleService<SysTaskServiceImpl, SysTaskDao, SysTaskEntity>
        implements SysTaskService, IStartUp {

    @Resource
    private QuartzManager quartzManager;

    /**
     * 默认调度工厂
     */
    private Class jobClass = QuartzJobFactory.class;

    @Override
    public boolean addTask(String cronExpression, String jobName, String bizModule, String bizId, String bizTag,
                           String callbackData, TaskCallbackTypeEnum callbackType, String callbackUrl) {
        Assert.hasText(bizId, "业务id不能为空");
        try {
            SysTaskEntity task = SysTaskEntity.builder()
                    .cronExpression(cronExpression)
                    .jobName(jobName)
                    .bizModule(bizModule)
                    .bizId(bizId)
                    .bizTag(bizTag)
                    .callbackData(callbackData)
                    .callbackType(callbackType)
                    .callbackUrl(callbackUrl)
                    .createTime(new Date())
                    .build();
            super.save(task);
            quartzManager.addJob(task.getJobName() + "-" + task.getId(), jobClass, task);
            return true;
        } catch (Exception e) {
            logger.error("[{}] 添加定时任务失败，e={}", SysModuleConst.SYS, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 初始化/刷新任务
     */
    @Scheduled(cron = "0 0 0,12 * * ?")
    public void init() {
        logger.info("[{}] 开始加重定时任务...", SysModuleConst.SYS);
        quartzManager.clear();
        final List<SysTaskEntity> taskList = this.listEnabledAll();
        for (SysTaskEntity task : taskList) {
            quartzManager.addJob(task.getJobName() + "-" + task.getId(), jobClass, task);
        }
        quartzManager.startJob();
        logger.info("[{}] 清理所有定时任务结束，成功加载", SysModuleConst.SYS);
    }

    @Override
    public void startUp(ApplicationContext applicationContext) throws Exception {
        init();
    }

}
