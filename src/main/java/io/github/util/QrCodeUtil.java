package io.github.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.util.file.FileUtils;
import io.github.util.http.HttpAttachment;
import io.github.util.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具类
 * 条形码、二维码生成
 * <dependency>
 * <groupId>com.google.zxing</groupId>
 * <artifactId>core</artifactId> & <artifactId>javase</artifactId>
 * <version>2.2</version>
 * </dependency>
 *
 * @author Created by 思伟 on 2019/12/17
 */
@Slf4j
public class QrCodeUtil {
    /**
     * PNG图片BASE64编码字符串前缀
     */
    private static final String IMAGE_PNG_BASE64_PREFIX = "data:image/png;base64,";

    /**
     * 默认输出格式名称
     */
    private static final String DEFAULT_OUTPUT_FORMAT_NAME = "png";

    /**
     * 生成条形码图像的多格式输出工厂类
     */
    private static final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    /**
     * 默认自定义图片配置
     */
    private static final MatrixToImageConfig DEFAULT_IMAGE_CONFIG =
            new MatrixToImageConfig(Color.BLACK.getRGB(), Color.WHITE.getRGB());
    /**
     * 黑色-十六进制颜色字符串
     *
     * @deprecated {@link Color#BLACK}
     */
    @Deprecated
    private static final int BLACK = 0xff000000;
    /**
     * 白色-十六进制颜色字符串
     *
     * @deprecated {@link Color#WHITE}
     */
    @Deprecated
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成条形码图像
     *
     * @param contents 内容
     * @param format   要生成的条形码格式
     * @param width    宽度(单位:像素)
     * @param height   高度(单位:像素)
     * @param hints    附加参数
     * @return BitMatrix
     * @throws WriterException
     */
    private static BitMatrix getEncode(String contents, BarcodeFormat format,
                                       int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        return multiFormatWriter.encode(contents, format, width, height, hints);
    }

    /**
     * 生成二维码图片字节流
     *
     * @return byte[]
     */
    public static byte[] encodeToBytes(String contents, BarcodeFormat format,
                                       int width, int height, Map<EncodeHintType, ?> hints) throws IOException, WriterException {
        try {
            BitMatrix bitMatrix = getEncode(contents, format, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                if (!ImageIO.write(image, DEFAULT_OUTPUT_FORMAT_NAME, out)) {
                    throw new IOException("Could not write an image of format " + DEFAULT_OUTPUT_FORMAT_NAME);
                }
                return out.toByteArray();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成二维码图片至指定输出流
     * 此方法调用后会不会自动关闭输出流
     * *如果需要，调用者有责任关闭流。
     *
     * @param outputStream 输出流
     * @see MatrixToImageWriter#writeToStream
     */
    public static void encodeToStream(OutputStream outputStream, String contents, BarcodeFormat format,
                                      int width, int height, Map<EncodeHintType, ?> hints) throws IOException, WriterException {
        try {
            BitMatrix bitMatrix = getEncode(contents, format, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            if (!ImageIO.write(image, DEFAULT_OUTPUT_FORMAT_NAME, outputStream)) {
                throw new IOException("Could not write an image of format " + DEFAULT_OUTPUT_FORMAT_NAME);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成二维码图片
     * 修改为UTF-8，否则中文编译后解析不了
     *
     * @param format 图片格式
     * @param file   生成二维码图片位置
     * @see MatrixToImageWriter#writeToFile
     */
    public static void encodeToFile(String contents, File file, BarcodeFormat format,
                                    int width, int height, Map<EncodeHintType, ?> hints) throws IOException, WriterException {
        try {
            BitMatrix bitMatrix = getEncode(contents, format, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            if (!ImageIO.write(image, DEFAULT_OUTPUT_FORMAT_NAME, file)) {
                throw new IOException("Could not write an image of format " + DEFAULT_OUTPUT_FORMAT_NAME + " to " + file);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成二维码内容
     *
     * @param matrix BitMatrix
     * @return BufferedImage
     * @see MatrixToImageWriter#toBufferedImage
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        return MatrixToImageWriter.toBufferedImage(matrix,
                DEFAULT_IMAGE_CONFIG);
        /*int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return image;*/
    }

    /**
     * 从Base64字符串中解析二维码内容（同时支持web格式全字符串）
     *
     * @param base64Str Base64格式二维码图片
     * @return 解析后的值
     */
    public static String decode(final String base64Str) throws IOException, NotFoundException {
        // 从分隔符第一次出现的位置向后截取
        final String base64String = StringUtils.defaultIfBlank(StringUtils.substringAfter(base64Str, IMAGE_PNG_BASE64_PREFIX), base64Str);
        final byte[] bytes = Base64.decodeBase64(base64String);
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            return decode(ImageIO.read(byteArrayInputStream));
        }
    }

    /**
     * 从文件中解析二维码内容
     *
     * @param file a <code>File</code> to read from.
     * @return 解析后的值
     */
    public static String decode(File file) throws IOException, NotFoundException {
        return decode(ImageIO.read(file));
    }

    /**
     * 解析二维码内容
     *
     * @param image 二维码图像数据
     * @return 解析后的值
     * @throws NotFoundException
     */
    public static String decode(BufferedImage image) throws NotFoundException {
        try {
            if (image == null) {
                log.warn("Could not decode image");
                return null;
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
            // 解码设置编码方式为：utf-8
            hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            Result result = new MultiFormatReader().decode(bitmap, hints);
            String decodedStr = result.getText();
            // log.info("解析后内容：" + decodedStr);
            return decodedStr;
        } catch (ReaderException re) {
            log.error("二维码解码错误，e=" + re.toString(), re);
            throw re;
        } catch (Exception ex) {
            log.error("二维码解码错误，e={}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * just test
     */
    public static void main(String[] args) throws IOException, WriterException,
            NotFoundException, NoSuchAlgorithmException {
        String colorStr = StringUtils.toString("0xFFFFFFFF").substring(4);
        Color color = new Color(Integer.parseInt(colorStr, 16));
        log.info("{}", color.getRGB());
        log.info("{}", Color.BLACK.getRGB());
        log.info("{}", Color.WHITE.getRGB());


        File file = new File("D://test.png");
        /**
         * 在`com.google.zxing.MultiFormatWriter`类中，定义了一些我们不知道的码,二维码只是其中的一种
         */
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(2);
        // 容错级别，H是最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 上线左右的空白边距
        hints.put(EncodeHintType.MARGIN, 3);
        encodeToFile(
                "https://zhousiwei.gitee.io/ibooks/",
                file, BarcodeFormat.QR_CODE, 430, 430, hints);
        log.info(decode(file));

        String codeUrl = "http://vt.ge.cflac.org.cn/usr/captcha";
        codeUrl = "http://bootplus.diandianys.com/admin/captcha.jpg";
        codeUrl = "http://bootplus.diandianys.com/share/qrcode";

        final byte[] bytes = encodeToBytes(codeUrl, BarcodeFormat.QR_CODE, 130, 130, hints);
        final String base64Str = Base64.encodeBase64String(bytes);
        log.info(decode(IMAGE_PNG_BASE64_PREFIX.concat(base64Str)));
        log.info(decode(base64Str));

        final HttpAttachment attachment = new HttpClient().download(codeUrl);
        final String path = FileUtils.getTempDirectoryPath().concat(attachment.getFullName());
        attachment.toFile(path);
//        log.info(decode(new File("C:\\Users\\24343\\AppData\\Local\\Temp\\d48284004f6cb06f6fbdf075ab19028d.jpg")));
        log.info(decode(new File(path)));

        // TODO java读取验证码值

    }

}
