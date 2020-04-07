package io.github.util.file;

import io.github.common.enums.IEnum;

/**
 * 文件类型定义
 *
 * @author Created by 思伟 on 2020/1/8
 */
public enum FileTypeEnum implements IEnum {
    /**
     * 图片
     */
    IMAGE("IMAGE", "图片"),
    /**
     * 音频文件
     */
    AUDIO("AUDIO", "音频文件"),
    /**
     * 视频文件
     */
    VIDEO("VIDEO", "视频文件"),
    /**
     * 压缩文件
     */
    ZIP("ZIP", "压缩文件"),
    /**
     * 文档文件
     */
    DOC("DOC", "文档文件"),
    /**
     * 文本文件
     */
    TEXT("TEXT", "文本文件"),
    /**
     * word excel等其他文件
     */
    OTHER("OTHER", "其他文件");

    private String key;

    private String value;

    private FileTypeEnum(String key, String value) {
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
