package io.github.controller;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.config.aop.annotation.MyController;
import io.github.config.aop.annotation.MyLog;
import io.github.frame.controller.AbstractController;
import io.github.util.QrCodeUtil;
import io.github.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * TODO 如果这里使用了注入IOC的对象，手动注册控制器是否还可用且注入对象不为空
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
@Slf4j
//@Controller
//@Component
@MyController
@RequestMapping("/share/qrcode")
public class QrCodeController extends AbstractController<QrCodeController> {

    private MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

    /**
     * 二维码生成
     *
     * @param width  宽度
     * @param height 高度
     * @param text   内容（可以为URL或者文本）
     */
    @MyLog
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
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET.name());
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

    /**
     * 测试接口
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    protected Map<?, ?> test()
            throws InterruptedException {
        Map<String, CharSequence> map = Maps.newConcurrentMap();
        map.put("test", "1");
        map.put("random", StringUtils.join(T_VERSION, "_", secureRandom.nextInt(2 << 18)));
        Thread.sleep(3000L);
        return map;
    }

}

