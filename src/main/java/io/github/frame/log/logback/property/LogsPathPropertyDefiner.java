package io.github.frame.log.logback.property;

import io.github.util.YamlUtil;

/**
 * 日志路径获取
 *
 * @author Created by 思伟 on 2020/5/19
 */
public class LogsPathPropertyDefiner extends BasePropertyDefiner {

    /**
     * 日志路径参数key
     */
    private final String logPathPropertyKey = "application.logs.path";

    /**
     * 项目名称
     */
    private String projectName;

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getValue() {
        String logsPath = null;
        try {
            logsPath = YamlUtil.getProperty(logPathPropertyKey);
        } catch (Throwable e) {
            addError(getSimpleErrMsg(), e);
        } finally {
            if (isBlank(logsPath) && !isBlank(projectName)) {
                addInfo("use default logsPath!");
                logsPath = System.getProperties().getProperty("user.home")
                        .concat("/logs/").concat(this.projectName).concat("_logs");
            }
        }
        addInfo("set logsPath: " + logsPath);
        return logsPath;
    }

    @Override
    protected String getSimpleErrMsg() {
        return "Load logsPath from yaml failed";
    }

}
