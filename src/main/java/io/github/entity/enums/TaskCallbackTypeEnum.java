package io.github.entity.enums;

import io.github.common.enums.IEnum;

/**
 * 定时任务回调类型
 *
 * @author Created by 思伟 on 2020/8/21
 */
public enum TaskCallbackTypeEnum implements IEnum {
    PRINT("PRINT", "打印"),
    CLASS("CLASS", "类回调"),
    HTTP("HTTP", "HTTP请求"),
    HESSIAN("HESSIAN", "HESSIAN请求");

    private String key;

    private String value;

    TaskCallbackTypeEnum(String key, String value) {
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
