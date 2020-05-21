package io.github.frame.log.logback.property;

import io.github.util.YamlUtil;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * 读取Yaml属性
 *
 * @author Created by 思伟 on 2020/5/19
 */
public class YamlPropertyDefiner extends BasePropertyDefiner {

    /**
     * 参数key
     */
    private String propertyKey;

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    @Override
    public String getPropertyValue() {
        return Optional.ofNullable(this.propertyKey).map(str -> {
            try {
                // 获取yaml文件对应的key值
                String propertyValue = YamlUtil.getProperty(this.propertyKey);
                addInfo(MessageFormat.format("get propertyKey=[{0}] value=[{1}] "
                        , propertyKey, propertyValue));
                return propertyValue;
            } catch (IOException e) {
                addError(getSimpleErrMsg(), e);
            }
            return null;
        }).orElseThrow(() -> new IllegalArgumentException(getSimpleErrMsg()));
    }

    @Override
    protected String getSimpleErrMsg() {
        String errMsg = "Load propertyValue from yaml failed";
        return String.format("%s, propertyKey = 【%s】", errMsg, this.propertyKey);
    }

}
