package io.github.base;

import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * SpringBoot测试Mvc基类
 *
 * @author Created by 思伟 on 2020/9/21
 */
@AutoConfigureMockMvc
public abstract class BaseAppBootMvcTest extends BaseAppTest {

    @Resource
    protected MockMvc mvc;

    @Resource
    protected WebApplicationContext webApplicationContext;

    @Before
//    @BeforeClass
    public void setUp() throws Exception {
    }

}
