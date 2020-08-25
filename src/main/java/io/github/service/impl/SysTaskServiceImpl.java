package io.github.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.dao.SysTaskDao;
import io.github.entity.SysTaskEntity;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.constant.SysModuleConst;
import io.github.frame.prj.service.SimpleService;
import io.github.frame.spring.IStartUp;
import io.github.service.SysTaskService;
import io.github.task.quartz.QuartzJobFactory;
import io.github.task.quartz.QuartzManager;
import io.github.util.MethodNameUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    protected boolean onlyEnabled() {
        return false;
    }

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
            quartzManager.addJob(getJobName(task), jobClass, task);
            return true;
        } catch (Exception e) {
            logger.error("[{}] 添加定时任务失败，e={}", SysModuleConst.SYS, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 构建唯一任务名称
     *
     * @param task 定时任务
     * @return String
     */
    private String getJobName(SysTaskEntity task) {
        return task.getJobName() + "-" + task.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnable(Long id, boolean enabled) {
        final boolean enable = super.updateEnable(id, enabled);
        if (!enable) {
            // 更新失败不再进行操作
            return false;
        }
        final SysTaskEntity entity = this.getById(id);
        // 更新定时任务
        if (enabled) {
            quartzManager.addJob(getJobName(entity), jobClass, entity);
        } else {
            quartzManager.removeJob(getJobName(entity));
        }
        return true;
    }

    @Override
    public Page<SysTaskEntity> getPage(Integer offset, Integer limit, String sort, Boolean isAsc, SysTaskEntity entity) {
        final QueryWrapper<SysTaskEntity> wrapper = Wrappers.query();
        if (StringUtils.isNoneBlank(sort) && null != isAsc) {
            wrapper.orderBy(true, isAsc, MethodNameUtil.camel2underStr(sort));
        }
        if (StringUtils.isNoneBlank(entity.getJobName())) {
            wrapper.lambda().like(SysTaskEntity::getJobName, StringUtils.trim(entity.getJobName()));
        }
        Page<SysTaskEntity> page = new Page<>(offset, limit);
        return this.page(page, wrapper);
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
            quartzManager.addJob(getJobName(task), jobClass, task);
        }
        quartzManager.startJob();
        logger.info("[{}] 清理所有定时任务结束，成功加载", SysModuleConst.SYS);
    }

    @Override
    public void startUp(ApplicationContext applicationContext) throws Exception {
        init();
    }

}
