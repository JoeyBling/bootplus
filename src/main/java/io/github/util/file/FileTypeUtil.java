package io.github.util.file;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 根据文件头获取文件扩展名
 * <br/>
 * 可以参考 <a href='http://www.zhihu.com/question/22018894'>magicNumber</a>相关
 * <p/> <a href='http://blog.csdn.net/fenglibing/article/details/7733496'>magicNumber2</a>
 *
 * @author Created by 思伟 on 2020/1/8
 * @see FileTypeCheckUtil
 */
public class FileTypeUtil {

    /**
     * 通过文件头获取文件扩展名
     */
    private static Map<String, String> magicNumberMap = new HashMap<String, String>();
    /**
     * 通过http content-type获取文件扩展名
     */
    private static Map<String, String> contentTypeMap = new HashMap<String, String>();
    /**
     * 通过文件扩展名获取文件类型
     */
    private static Map<String, FileTypeEnum> typeMap = new HashMap<String, FileTypeEnum>();

    static {
        magicNumberMap.put("FFD8FFE0", ".jpg"); // JPEG
        magicNumberMap.put("FFD8FFE1", ".jpg"); // JPEG
        magicNumberMap.put("89504E47", ".png"); // PNG
        magicNumberMap.put("47494638", ".gif"); // GIF
        magicNumberMap.put("424D", ".bmp"); // BMP
        magicNumberMap.put("4D4D002A", ".tif"); // TIF
        magicNumberMap.put("49492A00", ".tif"); // TIF
        magicNumberMap.put("52494646", ".webp"); // WEBP

        magicNumberMap.put("000000", ".mp4"); // mp4

        magicNumberMap.put("49443303", ".amr"); // amr
        magicNumberMap.put("2321414D", ".amr"); // amr

        contentTypeMap.put("image/jpeg", ".jpg"); // JPG
        contentTypeMap.put("image/jpg", ".jpg"); // JPG
        contentTypeMap.put("image/png", ".png"); // PNG
        contentTypeMap.put("image/gif", ".gif"); // gif
        contentTypeMap.put("image/vnd.wap.wbmp", ".wbmp"); // wbmp

        typeMap.put(".jpg", FileTypeEnum.IMAGE);
        typeMap.put(".png", FileTypeEnum.IMAGE);
        typeMap.put(".gif", FileTypeEnum.IMAGE);
        typeMap.put(".bmp", FileTypeEnum.IMAGE);
        typeMap.put(".tif", FileTypeEnum.IMAGE);
        typeMap.put(".webp", FileTypeEnum.IMAGE);

        typeMap.put(".mp3", FileTypeEnum.AUDIO);
        typeMap.put(".acc", FileTypeEnum.AUDIO);
        typeMap.put(".amr", FileTypeEnum.AUDIO);
        typeMap.put(".wav", FileTypeEnum.AUDIO);

        typeMap.put(".mp4", FileTypeEnum.VIDEO);
        typeMap.put(".avi", FileTypeEnum.VIDEO);
    }

    public static FileTypeEnum getFileType(String extensionName) {
        FileTypeEnum fileType = typeMap.get(extensionName);
        if (null == fileType) {
            return FileTypeEnum.OTHER;
        }
        return fileType;
    }

    public static String getExtensionName(String path) throws IOException {
        FileInputStream is = new FileInputStream(path);
        String extName = getExtensionName(is);
        if (extName == null && path.lastIndexOf(".") != -1) {
            String _extName = path.substring(path.lastIndexOf("."));
            // 扩展名一般超过5位，超过5位的
            extName = _extName.length() > 5 ? extName : _extName;
        }
        return extName;
    }

    public static String getExtensionName(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        String extName = getExtensionName(is);
        if (extName == null && file.getName().lastIndexOf(".") != -1) {
            String name = file.getName();
            String _extName = name.substring(name.lastIndexOf("."));
            // 扩展名一般超过5位，超过5位的
            extName = _extName.length() > 5 ? extName : _extName;
        }
        return extName;
    }

    public static String getExtensionName(FileInputStream is) throws IOException {
        try {
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            String extName = null;
            do {
                extName = magicNumberMap.get(bytesToHexString(b));
                if (StringUtils.isNotBlank(extName)) {
                    return extName;
                }

                byte[] _b = b;
                b = new byte[b.length - 1];

                copyArray(_b, b);

            } while (b.length > 2);

            return extName;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static void copyArray(byte[] from, byte[] to) {
        for (int i = 0; i < to.length && i < from.length; i++) {
            to[i] = from[i];
        }
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String getExtensionNameByContentType(String contentType) {
        String extName = contentTypeMap.get(contentType.toLowerCase());
        if (StringUtils.isBlank(extName)) {
            Iterator<Map.Entry<String, String>> iter = contentTypeMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String val = entry.getValue();
                if (StringUtils.startsWithIgnoreCase(contentType.trim(), key)) {
                    return val;
                }
            }
        }
        return extName;
    }

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("f:\\A_lAFwRP7_vsIxd.amr");
        byte[] b = new byte[4];
        fis.read(b, 0, b.length);
        System.out.println(bytesToHexString(b));

    }
}
