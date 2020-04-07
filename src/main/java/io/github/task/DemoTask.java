package io.github.task;

import io.github.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 定时任务
 *
 * @author Created by 思伟 on 2019/12/17
 */
@Slf4j
@Component
public class DemoTask implements InitializingBean {

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
    public void afterPropertiesSet() throws Exception {
    }

}
