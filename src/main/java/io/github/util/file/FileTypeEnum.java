package io.github.util.file;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件类型定义
 *
 * @author Created by 思伟 on 2020/1/8
 */
public enum FileTypeEnum {
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

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public static FileTypeEnum getByKey(String key) {
        FileTypeEnum[] es = FileTypeEnum.values();
        for (int i = 0; i < es.length; i++) {
            if (es[i].getKey().equals(key)) {
                return es[i];
            }
        }
        return null;
    }

    public static String[] toArray(List<FileTypeEnum> enumList) {
        if (null == enumList || enumList.isEmpty()) {
            return null;
        }
        String[] status = new String[enumList.size()];
        for (int i = 0; i < enumList.size(); i++) {
            status[i] = enumList.get(i).getKey();
        }
        return status;
    }

    public static List<FileTypeEnum> fromArray(String[] typeNameList) {
        List<FileTypeEnum> enumList = new ArrayList<FileTypeEnum>();
        FileTypeEnum e;
        for (int i = 0; i < typeNameList.length; i++) {
            e = getByKey(typeNameList[i]);
            if (null == e) {
                continue;
            }
            enumList.add(e);
        }
        return enumList;
    }

}
