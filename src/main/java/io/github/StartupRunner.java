package io.github;

import io.github.config.ApplicationProperties;
import io.github.config.aop.annotation.MyAutowired;
import io.github.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 启动SpringBoot会执行 @Order标识执行顺序 值越小越先执行
 *
 * @author Created by 思伟 on 2019/12/17
 */
@Component
@Order(1)
@Slf4j
public class StartupRunner implements CommandLineRunner {

    /**
     * Spring线程池
     */
    @Resource
    private TaskExecutor taskExecutor;

    @MyAutowired
    private ApplicationProperties applicationProperties;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>服务启动完成，执行加载数据等操作....<<");
        taskExecutor.execute(() -> {
            log.info("当前线程名称：{}", Thread.currentThread().getName());
            log.info("Real thread begin to execute!==={}", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            if (null != applicationProperties) {
                log.info("项目：{}[{}]启动成功，项目地址：{}，描述：{}",
                        applicationProperties.getName(), applicationProperties.getVersion(),
                        applicationProperties.getUrl(), applicationProperties.getDescription());
            }
        });
        // Test
    }

}
