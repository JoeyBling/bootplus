package io.github.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;

import java.net.URL;

/**
 * 更改 logback.xml 文件位置
 * TODO 无法重写
 *
 * @author Created by 思伟 on 2020/3/18
 */
@Deprecated
public class MyContextInitializer extends ContextInitializer {

    public MyContextInitializer(LoggerContext loggerContext) {
        super(loggerContext);
    }

    @Override
    public URL findURLOfDefaultConfigurationFile(boolean updateStatus) {
        URL configUrl = super.findURLOfDefaultConfigurationFile(updateStatus);
        if (configUrl != null) {
            return configUrl;
        }
        return configUrl;
//        return super.getResource(AUTOCONFIG_FILE, myClassLoader, updateStatus);;
    }
}
