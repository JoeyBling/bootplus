package io.github.util.sketch;

import com.google.common.collect.Maps;
import io.github.frame.prj.exception.RRException;
import io.github.util.file.FileUtils;
import io.github.util.sketch.img.Img;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 字体工具类
 *
 * @author Created by 思伟 on 2020/3/4
 */
@Slf4j
public class FontUtil {

    /**
     * 默认字体存放目录
     */
    protected final static String FONTS_DIR = "jdk_fonts";

    /**
     * 字体缓存
     */
    private static final Map<String, Font> FONT_CACHE_MAP = Maps.newConcurrentMap();

    /**
     * @see #getFontPath(String, String)
     */
    public static String getFontPath(String fontName) {
        return getFontPath(FONTS_DIR, fontName);
    }

    /**
     * 读取jar包的文件流
     *
     * @param fontName 字体文件名
     */
    public static InputStream getFontInputStream(String fontName) {
        try {
            return FontUtil.class.getClassLoader().getResourceAsStream(
                    FileUtils.generateFileUrl(FONTS_DIR, fontName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RRException("路径获取失败，请联系服务提供商处理");
        }
    }

    /**
     * 获取字体路径
     *
     * @param fontDir  字体文件夹
     * @param fontName 字体文件名称
     */
    public static String getFontPath(String fontDir, String fontName) {
        Assert.notNull(fontName, "fontName must not be null");
        String classPath;
        try {
            classPath = FontUtil.class.getClassLoader().getResource("").toURI().getPath();
            return FileUtils.generateFileUrl(classPath, fontDir, fontName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RRException("路径获取失败，请联系服务提供商处理");
        }
    }

    /**
     * @see #loadFont(String, int, float)
     */
    public static Font loadFont(String fontFilePath) throws IOException {
        return loadFont(fontFilePath, Font.TRUETYPE_FONT, 12.0f);
    }

    /**
     * @see #loadFont(String)
     */
    public static Font loadFont(String fontFilePath, boolean isLoadPath) throws IOException {
        if (isLoadPath) {
            fontFilePath = getFontPath(fontFilePath);
        }
        return loadFont(fontFilePath);
    }

    public static Font loadFont(String fontFilePath, int style, float fontSize) throws IOException {
        return loadFont(fontFilePath, style, fontSize, null);
    }

    /**
     * 加载自定义字体
     *
     * @param fontFilePath 字体文件路径
     * @param style        字体类型
     * @param fontSize     字体大小
     * @param inputStream  字体文件输入流
     * @return Font
     */
    public static Font loadFont(String fontFilePath, int style, float fontSize, InputStream inputStream)
            throws IOException {
        String key = fontFilePath + "|" + style;
        Font dynamicFont = FONT_CACHE_MAP.get(key);
        if (dynamicFont == null) {
            try {
                if (null == inputStream) {
                    File file = new File(fontFilePath);
                    inputStream = new FileInputStream(file);
                }
                dynamicFont = Font.createFont(style, inputStream);
                FONT_CACHE_MAP.put(key, dynamicFont);
            } catch (Exception e) {
                log.error("字体加载异常：{}，加载默认字体【宋体】", e.getMessage());
                return new Font("宋体", Font.PLAIN, 14);
            } finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        Font dynamicFontPt = dynamicFont.deriveFont(fontSize);

        return dynamicFontPt;
    }

    /**
     * @see #getDefaultFont(int, float)
     */
    public static Font getDefaultFont() {
        return getDefaultFont(Font.PLAIN, 14.0f);
    }

    /**
     * 获取默认字体
     *
     * @param style    字体类型
     * @param fontSize 字体大小
     * @return Font
     */
    public static Font getDefaultFont(int style, float fontSize) {
        try {
            String fontPath = getFontPath("SIMLI.TTF");
            Font font = loadFont(fontPath, style, fontSize);
            return font;
        } catch (IOException e) {
            log.error("字体加载异常{}", e.getMessage());
//            throw new IOException(e.getMessage(), e.getCause());
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        // TODO Test 打成jar包是不是就不能读取了？
        System.out.println(FontUtil.getFontPath("SIMLI.TTF"));
        System.out.println(FontUtil.getDefaultFont());
        System.out.println(FontUtil.loadFont("others/zcool_gaoduanhei.ttf", true));
        Font font = FontUtil.loadFont("others/zcool_gaoduanhei.ttf", true);
        System.out.println(font);
        // 输出图片路径
        String outFilePath = new ClassPathResource("/file/default/").getFile().getPath() + "/test.jpg";
        // 生成图片
        Img.from(new File(new ClassPathResource("/file/default/defaultPatBanner.jpg").getFile().getPath()))
                // 医院名称（白底）
                .pressText(
                        "医院名称（蓝色上层）",
                        new Color(255, 255, 255),
                        font.deriveFont(44.0f),
                        33, 118, 1.0f
                )
                // 医院名称（蓝色上层）
                .pressText(
                        "医院名称（蓝色上层）",
                        new Color(16, 105, 170),
                        font.deriveFont(44.0f),
                        30, 115, 1.0f
                )
                .write(new File(outFilePath));
        log.info("输出图片路径为:{}", outFilePath);
    }

}
