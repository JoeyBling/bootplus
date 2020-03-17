package io.github.base;

import io.github.App;
import io.github.util.config.Constant;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 测试基类
 * 默认情况下，在每个 JPA 测试结束时，事务会发生回滚。这在一定程度上可以防止测试数据污染数据库
 * 测试持久层时，默认是回滚的。可以在具体的测试方法上添加@Rollback(false)来禁止回滚，也可以在测试类上添加
 * SpringBoot junit 全局过滤器和监听器会失效
 *
 * @author Joey
 * @Email 2434387555@qq.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@Transactional
// true ? 测试数据不会污染数据库 : 会真正添加到数据库当中
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(WebConfig.class)
public abstract class BaseAppTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    protected Constant constant;

}
