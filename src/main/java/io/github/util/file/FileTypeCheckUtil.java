package io.github.util.file;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型检查类 <br>
 * 根据文件头、扩展名判断文件是否合法
 *
 * @author Created by 思伟 on 2020/1/8
 */
public class FileTypeCheckUtil {

    private static Map<String, String> magicNumberMap = new HashMap<String, String>();

    static {
        // JPG 文件以FFD8开头(JFIF格式FFD8FFE0开头，Exif格式FFD8FFE1开头，JFIF为老版格式，Exif为新版格式)  FFD9结尾
        magicNumberMap.put("jpeg-s", "FFD8");
        magicNumberMap.put("jpeg-e", "FFD9");
        // PNG 文件以89504E470D0A1A0A开头
        magicNumberMap.put("png", "89504E470D0A1A0A");


        magicNumberMap.put("zip", "504B0304");
        magicNumberMap.put("rar", "52617221");
    }

    /**
     * 通过文件流
     *
     * @param is
     * @param extName
     * @return
     */
    public static boolean checkFile(InputStream is, String extName) {
        return false;
    }

    /**
     * 通过文件流判断是否是文件
     *
     * @param is
     * @return
     */
    public static boolean checkImage(InputStream is) {
        boolean flag = false;
        try {
            BufferedImage bufferedImage = ImageIO.read(is);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width == 0 || height == 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断是否zip文件
     *
     * @param is 文件输入流，前4个字符即可
     * @return
     */
    public static boolean checkZip(InputStream is) {
        return doCheck(is, magicNumberMap.get("zip"));
    }

    /**
     * 判断是否rar文件
     *
     * @param is 文件输入流，前4个字符即可
     * @return
     */
    public static boolean checkRar(InputStream is) {
        return doCheck(is, magicNumberMap.get("rar"));
    }

    /**
     * 判断是否png文件
     *
     * @param is 文件输入流，前8个字符即可
     * @return
     */
    public static boolean checkPng(InputStream is) {
        return doCheck(is, magicNumberMap.get("png"));
    }

    /**
     * 判断是否jpeg文件
     *
     * @param is 文件输入流，完整文件
     * @return
     */
    public static boolean checkJpeg(InputStream is) {
        boolean flag = false;
        byte[] sb = new byte[magicNumberMap.get("jpeg-s").length() / 2];
        byte[] eb = new byte[magicNumberMap.get("jpeg-e").length() / 2];
        try {
            // 读取头
            is.read(sb);
            // 跳过中间
            is.skip(is.available() - eb.length);
            // 读取尾
            is.read(eb);
            flag = StringUtils.equalsIgnoreCase(Hex.encodeHexString(sb), magicNumberMap.get("jpeg-s"))
                    && StringUtils.equalsIgnoreCase(Hex.encodeHexString(eb), magicNumberMap.get("jpeg-e"));
        } catch (IOException e) {
            flag = false;
        }
        return flag;
    }

    private static boolean doCheck(InputStream is, String magicNumber) {
        boolean flag = false;
        byte[] b = new byte[magicNumber.length() / 2];
        try {
            is.read(b);
            flag = StringUtils.equalsIgnoreCase(Hex.encodeHexString(b), magicNumber);
        } catch (IOException e) {
            flag = false;
        }
        return flag;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream is = new FileInputStream("D:\\backup\\Desktop\\B.jpg");
        System.out.println(checkJpeg(is));
    }

}
