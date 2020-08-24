package io.github.task;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import io.github.entity.SysTaskEntity;
import io.github.entity.enums.TaskCallbackTypeEnum;
import io.github.frame.prj.constant.SysModuleConst;
import io.github.frame.prj.util.TaskUtil;
import io.github.frame.spring.IStartUp;
import io.github.service.SysTaskService;
import io.github.service.impl.SysTaskCallbackServiceImpl;
import io.github.util.ClassUtil;
import io.github.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 定时任务
 * <p>
 * InitializingBean:初始化接口方法--等同于`init-method`
 * Spring先判断类是否实现了InitializingBean接口，是就调用其方法afterPropertiesSet().
 * 之后在对init-method进行判断是否执行init-method定义的方法
 *
 * @author Created by 思伟 on 2019/12/17
 * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#invokeInitMethods(String, Object, RootBeanDefinition)
 */
@Slf4j
@Component
public class DemoTask implements IStartUp {

    /**
     * 回调业务标识
     */
    private final String BIZ_TAG = "DEMO_TASK";
    @Resource
    private SysTaskService sysTaskService;

    /**
     * @Async 代表异步执行
     * MARK:默认程序启动就执行一次
     * 模拟定时获取Token
     * 0 0/30 * * * ?（每隔30分钟）
     * 0 0/15 * * * ?（每隔15分钟）
     * 0 0 0/1 * * ?（每隔1个小时）
     * {http://www.bejson.com/othertools/cron/}
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
//    @Scheduled(fixedRate = 1000 * 60 * 1)
//    @Scheduled(cron = "* * * * * ?")
    @PostConstruct
    @Async
//    @Async(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public void timingGetToken() {
        log.info("当前线程名称：{}", Thread.currentThread().getName());
        log.info("模拟定时任务==={}", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
    }

    @Override
    public void startUp(ApplicationContext applicationContext) throws Exception {
        final Date nowDate = new Date();
        final SysTaskEntity entity = SysTaskEntity.builder()
                .cronExpression(TaskUtil.getCronForSecondsAfter(nowDate, 20))
                .jobName("测试定时任务")
                .bizModule(SysModuleConst.DEMO).bizId("DEMO").bizTag(BIZ_TAG)
                .callbackData(JSON.toJSONString(MapUtil.builder()
                        .put("time", DateUtils.format(nowDate))
                        .put("type", "DEMO_TYPE").build()))
                .callbackUrl(ClassUtil.getClass(this)).build();
        final TaskCallbackTypeEnum[] typeEnums = TaskCallbackTypeEnum.values();
        for (TaskCallbackTypeEnum typeEnum : typeEnums) {
            // 添加Demo
            sysTaskService.addTask(entity.getCronExpression(), entity.getJobName(),
                    entity.getBizModule(), entity.getBizId(), entity.getBizTag(),
                    entity.getCallbackData(), typeEnum, entity.getCallbackUrl());
        }
    }

    @Service
    public class DemoBizTaskCallbackService implements SysTaskCallbackServiceImpl.IBizTaskCallbackService {

        @Override
        public String getBizModule() {
            return SysModuleConst.DEMO;
        }

        @Override
        public String getBizTag() {
            return BIZ_TAG;
        }

        @Override
        public void execute(String jobName, String bizId, String data, String callbackUrl) {
            log.debug("业务类回调定时任务,jobName={},bizId={},data={}", jobName, bizId, data);
        }

    }

}
