package io.github.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码帮助类
 *
 * @author Created by 思伟 on 2019/12/17
 */
@Slf4j
public class QrCodeUtil {

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成QRCode二维码
     * 修改为UTF-8，否则中文编译后解析不了
     */
    public static void encode(String contents, File file, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws IOException, WriterException {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height, hints);
            writeToFile(bitMatrix, "png", file);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成二维码图片
     *
     * @param matrix BitMatrix
     * @param format 图片格式
     * @param file   生成二维码图片位置
     * @throws IOException
     */
    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        ImageIO.write(image, format, file);
    }

    /**
     * 生成二维码内容
     *
     * @param matrix BitMatrix
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 解析QRCode二维码
     */
    public static void decode(File file) throws NotFoundException, IOException {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            if (image == null) {
                log.error("Could not decode image");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                    source));
            Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
            // 解码设置编码方式为：utf-8
            hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
            Result result = new MultiFormatReader().decode(bitmap, hints);
            String resultStr = result.getText();
            log.info("解析后内容：" + resultStr);
        } catch (IOException ioe) {
            log.error("二维码解码错误，e=" + ioe.toString(), ioe);
            throw ioe;
        } catch (ReaderException re) {
            log.error("二维码解码错误，e=" + re.toString(), re);
            throw re;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    public static void main(String[] args) throws IOException, WriterException, NotFoundException {
        File file = new File("D://test.png");
        /**
         * 在`com.google.zxing.MultiFormatWriter`类中，定义了一些我们不知道的码,二维码只是其中的一种
         */
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 容错级别，H是最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 上线左右的空白边距
        hints.put(EncodeHintType.MARGIN, 3);
        QrCodeUtil.encode(
                "http://weixin.qq.com/q/5DhdFFfla5ESfq8dyRCq",
                file, BarcodeFormat.QR_CODE, 430, 430, hints);
        QrCodeUtil.decode(file);
    }

}
