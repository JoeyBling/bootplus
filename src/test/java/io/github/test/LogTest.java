package io.github.test;

import io.github.base.BaseAppTest;
import io.github.util.log.LogUtil;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * 日志测试类
 *
 * @author Created by 思伟 on 2020/3/17
 */
public class LogTest extends BaseAppTest {

    /**
     * 输出文件
     */
    @Test
    public void testLogForFile() {
        Logger logger = LogUtil.getInstance().getFileStatementLogger();
        logger.debug("debug file Statement");
        logger.warn("warn file Statement");
        logger.error("error file Statement");
    }

}
