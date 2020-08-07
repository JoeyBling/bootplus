package io.github.controller.admin;

import junit.framework.TestCase;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 组合单元测试
 *
 * @author Created by 思伟 on 2020/7/20
 */
@RunWith(Categories.class)
@Suite.SuiteClasses({
        SysUserControllerTest.class,
})
public class ControllerTestCase extends TestCase {

}
