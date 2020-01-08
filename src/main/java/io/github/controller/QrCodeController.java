package io.github.controller;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.util.QrCodeUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 二维码生成器
 * 条形码、二维码生成
 * shiro settings ---> /share/qrcode = anon
 * <dependency>
 * <groupId>com.google.zxing</groupId>
 * <artifactId>core</artifactId> & <artifactId>javase</artifactId>
 * <version>2.2</version>
 * </dependency>
 *
 * @author Created by 思伟 on 2019/11/29
 */
@Controller
@RequestMapping("/share/qrcode")
public class QrCodeController {

    private MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

    /**
     * 二维码生成
     *
     * @param width  宽度
     * @param height 高度
     * @param text   内容（可以为URL或者文本）
     */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public void qrCode(@RequestParam(required = false, value = "w", defaultValue = "200") Integer width,
                       @RequestParam(required = false, value = "h", defaultValue = "200") Integer height,
                       @RequestParam(required = false, defaultValue = "https://github.com/JoeyBling") String text,
                       HttpServletResponse response) throws IOException, WriterException {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache");
        // MediaType.IMAGE_JPEG_VALUE
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        Map<EncodeHintType, Object> hints = Maps.newConcurrentMap();
        // 生成二维码
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // 容错级别，H是最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 上线左右的空白边距
        hints.put(EncodeHintType.MARGIN, 3);
        BitMatrix bitMatrix = multiFormatWriter.encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage bufferedImage = QrCodeUtil.toBufferedImage(bitMatrix);
        try (ServletOutputStream out = response.getOutputStream()) {
            // MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            ImageIO.write(bufferedImage, "png", out);
        }
    }

}
