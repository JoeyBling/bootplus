package io.github.util.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;

/**
 * 文件工具类
 *
 * @author Created by 思伟 on 2019/12/31
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 校验文件名是否是所属文件类型(不区分大小写)
     *
     * @param fileName  文件名
     * @param fileTypes 文件类型
     * @return boolean
     */
    public final static boolean validateFileType(String fileName, CharSequence... fileTypes) {
        if (!ObjectUtils.isEmpty(fileName) && !ObjectUtils.isEmpty(fileTypes)) {
            for (final CharSequence fileType : fileTypes) {
                if (StringUtils.endsWithIgnoreCase(fileName, fileType)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 把字节数B转化为KB、MB、GB
     *
     * @param size 字节数B
     */
    public final static String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        int increasing = 1024;
        if (size < increasing) {
            return String.valueOf(size) + "B";
        } else {
            size = size / increasing;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < increasing) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / increasing;
        }
        if (size < increasing) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / increasing;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 拼接文件路径(同时会转换\或/出现2次以上的转/)推荐使用
     *
     * @param url 文件路径
     * @return 拼接后的文件路径
     */
    public static final String generateFileUrl(final String... url) {
        if (null == url) {
            return null;
        }
        // StringUtils.join自动过滤Null值
        String uri = StringUtils.join(url, StringUtils.equals(
                File.separator, "\\") ? "/" : File.separator);
        // (?i)在前面 不区分大小写
        return uri.replaceAll("(\\\\|/)+", "/");
    }

}
