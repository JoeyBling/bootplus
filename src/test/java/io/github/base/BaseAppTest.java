package io.github.base;

import io.github.App;
import io.github.config.ApplicationProperties;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

/**
 * 测试基类
 * 默认情况下，在每个 JPA 测试结束时，事务会发生回滚。这在一定程度上可以防止测试数据污染数据库
 * 测试持久层时，默认是回滚的。可以在具体的测试方法上添加@Rollback(false)来禁止回滚，也可以在测试类上添加
 * 由于Test没有启动web容器，所以SpringBoot junit 全局过滤器和监听器会失效
 * 如果加上了@Transactional注解，读取的数据都有缓存
 *
 * @author Created by 思伟 on 2020/6/6
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
//@Transactional
// true ? 测试数据不会污染数据库 : 会真正添加到数据库当中
//@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(WebConfig.class)
public abstract class BaseAppTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ApplicationProperties applicationProperties;

    /**
     * 获取Junit环境配置
     *
     * @return ApplicationProperties.JunitEnvConfig
     */
    protected ApplicationProperties.JunitEnvConfig getJunitEnv() {
        return Optional.ofNullable(applicationProperties.getJunitEnv())
                .orElse(new ApplicationProperties.JunitEnvConfig());
    }

    /**
     * 防止程序提前结束
     * 不建议使用，会导致事务出问题
     */
    @Deprecated
    protected void avoidExit() {
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
