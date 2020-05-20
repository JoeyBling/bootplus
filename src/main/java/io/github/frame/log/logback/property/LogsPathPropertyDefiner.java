package io.github.frame.log.logback.property;

import io.github.util.YamlUtil;

import java.io.IOException;

/**
 * 日志路径获取
 *
 * @author Created by 思伟 on 2020/5/19
 */
public class LogsPathPropertyDefiner extends BasePropertyDefiner {

    /**
     * 项目名称
     */
    private String projectName;

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getPropertyValue() {
        String logsPath = null;
        try {
            // 日志路径参数key
            String logPathPropertyKey = "application.logs.path";
            logsPath = YamlUtil.getProperty(logPathPropertyKey);
        } catch (IOException e) {
            addError("Load logsPath from yaml failed, will use default logsPath!");
        } finally {
            if (isBlank(logsPath) && !isBlank(projectName)) {
                logsPath = System.getProperties().getProperty("user.home")
                        .concat("/logs/").concat(this.projectName).concat("logs");
            }
        }
        addInfo("set logsPath: " + logsPath);
        return logsPath;
    }
}
