package io.github.entity.enums;

import io.github.common.enums.IEnum;

/**
 * 菜单类型
 *
 * @author Created by 思伟 on 2020/4/1
 */
public enum SysMenuTypeEnum implements IEnum {
    /**
     * 目录
     */
    CATALOG("CATALOG", "目录"),
    /**
     * 菜单
     */
    MENU("MENU", "菜单"),
    /**
     * 按钮
     */
    BUTTON("BUTTON", "按钮");

    private String key;

    private String value;

    SysMenuTypeEnum(String key, String value) {
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
