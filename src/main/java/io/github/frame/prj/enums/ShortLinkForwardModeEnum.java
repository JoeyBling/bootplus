package io.github.frame.prj.enums;

import io.github.common.enums.IEnum;

/**
 * 短链接前进模式
 *
 * @author Created by 思伟 on 2020/9/7
 */
public enum ShortLinkForwardModeEnum implements IEnum {
    GHOST("GHOST", "伪装"),
    REDIRECT("REDIRECT", "301重定向"),
    /**
     * 默认前进模式
     */
    DEFAULT(GHOST.key, GHOST.value);

    private String key;
    private String value;

    ShortLinkForwardModeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
