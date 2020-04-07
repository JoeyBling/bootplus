package io.github.util.file;

import io.github.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 文件帮助类
 *
 * @author Created by 思伟 on 2019/12/31
 * @see FileUtils
 */
@Deprecated
public class FileUtil {

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return boolean
     */
    public static boolean isExists(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    /**
     * 判断服务器文件是否存在
     *
     * @param path    文件路径
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isExists(String path, HttpServletRequest request) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(request.getServletContext().getRealPath(path));
        return file.exists();
    }

    /**
     * 获取服务器的文件
     *
     * @param path    文件路径
     * @param request HttpServletRequest
     * @return boolean
     */
    public static String getRealPath(String path, HttpServletRequest request) {
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("Null Path");
        }
        return request.getServletContext().getRealPath(path);
    }

    /**
     * 创建父级文件夹
     *
     * @param file 完整路径文件名(注:不是文件夹)
     */
    public static void createParentPath(File file) {
        File parentFile = file.getParentFile();
        if (null != parentFile && !parentFile.exists()) {
            // 创建文件夹
            parentFile.mkdirs();
            // 递归创建父级目录
            createParentPath(parentFile);
        }
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            // 删除目录失败，文件不存在
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

}
