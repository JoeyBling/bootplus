package io.github.util.file;

import io.github.util.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 文件工具类
 *
 * @author Created by 思伟 on 2019/12/31
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 文件路径前缀
     */
    public static final String FILE_PREFIX = "file:/";

    /**
     * @see java.net.URL#toURI()
     */
    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * 为给定的位置字符串创建一个URI实例，先用%20 URI编码替换空格。
     *
     * @param location 要转换为URI实例的位置字符串
     * @return the URI实例
     * @throws URISyntaxException 如果位置不是有效的URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return URI.create(StringUtils.replace(generateFileUrl(location),
                " ", "%20"));
    }

    /**
     * 获取文件，进行编码处理（防止中文乱码）
     *
     * @param fileUrl 文件路径
     * @throws URISyntaxException
     */
    public static File getFile(final String fileUrl) throws URISyntaxException {
        String location = fileUrl;
        if (!StringUtils.startsWithIgnoreCase(location, FILE_PREFIX)) {
            location = FILE_PREFIX.concat(location);
        }
        return new File(toURI(location).getSchemeSpecificPart());
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return boolean
     */
    public static boolean isExists(final String path) throws URISyntaxException {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        return isExists(getFile(path));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return boolean
     */
    public static boolean isExists(final File file) {
        if (null == file) {
            return false;
        }
        return file.exists();
    }

    /**
     * 强制创建父级文件夹
     *
     * @param file File
     * @throws IOException
     */
    public static void forceMkdirParent(final File file) throws IOException {
        if (file.isDirectory() && !isExists(file)) {
            forceMkdir(file);
        } else if (!isExists(file)) {
            final File parent = file.getParentFile();
            if (parent == null) {
                return;
            }
            if (!isExists(parent)) {
                forceMkdir(parent);
            }
        }
    }

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
     * For windows style path, we replace '\' to '/'.
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
        // Matcher.quoteReplacement(File.separator)
        return uri.replaceAll("(\\\\|/)+", "/")
                .replaceFirst("(?i)((ht|f)tp\\:(\\\\|/)+)", "http://")
                .replaceFirst("(?i)((ht|f)tps\\:(\\\\|/)+)", "https://");
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(getFile("/home/fulei/zhousiwei/upload"));
        System.out.println(getFile("D:/siwei"));
        forceMkdirParent(getFile("F:\\特扬网络\\WIFI.txt"));
        forceMkdirParent(getFile("F:\\特扬网络\\"));
        forceMkdirParent(getFile(FileUtils.class.getResource("/banner.txt").getPath()));
        System.out.println(toURI("https://blog.csdn.net/qq_30930805"));
        System.out.println(toURI("F:\\特扬网络\\WIFI.txt").getSchemeSpecificPart());
        System.out.println(toURI(FILE_PREFIX.concat("F:\\特扬网络\\WIFI.txt")).getSchemeSpecificPart());
        System.out.println(toURI(FILE_PREFIX.concat("F:\\特扬网络\\WIFI.txt")).getScheme());
    }

}
