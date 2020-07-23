package io.github.controller;

import io.github.config.MyWebAppConfigurer;
import io.github.frame.controller.AbstractController;
import io.github.util.DateUtils;
import io.github.util.R;
import io.github.util.StringUtils;
import io.github.util.exception.RRException;
import io.github.util.file.FileUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 文件上传下载控制器
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Controller
@RequestMapping("/file")
public class SysFileController extends AbstractController {

    /**
     * 文件上传(上传后返回保存的相对路径)---此处没有做权限验证
     *
     * @param uploadType 上传文件类型(不同保存的文件夹就不同)
     * @param request    HttpServletRequest
     * @return Map
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R upload(Integer uploadType, HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest;
        // 判断request是否有文件上传
        if (ServletFileUpload.isMultipartContent(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
        } else {
            return R.error("请先选择上传的文件");
        }
        // 存入数据库的相对路径
        String fileContextPath = null;
        Iterator<String> ite = multipartRequest.getFileNames();
        while (ite.hasNext()) {
            MultipartFile file = multipartRequest.getFile(ite.next());
            // 判断上传的文件是否为空
            if (file == null) {
                return R.error("上传文件为空");
            }
            // request.getServletContext().getRealPath(uploadPath)
            // 如果打成了jar包，Linux路径会变成/tmp/tomcat-docbase.*.*/
            String fileName = file.getOriginalFilename();
            logger.info("上传的文件原名称:{}", fileName);
            // 上传文件类型
            String fileType = fileName.indexOf(".") != -1
                    ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;

            logger.info("上传文件类型:{}", StringUtils.defaultString(fileType));
            // 自定义的文件名称
            String trueFileName = getTrueFileName(fileName, uploadType);
            // 防止火狐等浏览器不显示图片
            fileContextPath = FileUtils.generateFileUrl(
                    MyWebAppConfigurer.FILE_UPLOAD_PATH_EXT, trueFileName);
            // 上传文件保存的路径
            String uploadPath = FileUtils.generateFileUrl(
                    applicationProperties.getFileConfig().getUploadPath(), trueFileName);
            logger.debug("存放文件的路径:{}", uploadPath);
            // 上传文件后的保存路径
            File fileUpload = FileUtils.getFile(uploadPath);

            // 创建父级目录(Linux需要注意启动用户的权限问题)
            FileUtils.forceMkdirParent(fileUpload);

            file.transferTo(fileUpload);
            // 进行文件处理
            fileHandle(fileUpload);
            // 这里暂时只能上传一个文件
            break;
        }
        return R.ok().put("filePath", fileContextPath);
    }

    /**
     * 进行文件处理
     *
     * @param file File
     * @throws IOException
     */
    private void fileHandle(File file) throws IOException {
        try {
            BufferedImage buffered = ImageIO.read(file);
            int width = buffered.getWidth();
            int maxWidth = 800;
            if (width > maxWidth) {
                // TODO 压缩优化或添加动态配置
                logger.debug("进行图片压缩处理...");
                Thumbnails.of(file).height(maxWidth).toFile(file);
                logger.debug("图片压缩处理完毕...");
            }
        } catch (Exception e) {
            logger.warn("上传的文件不是图片");
        }
    }

    /**
     * 自定义上传文件的相对路径
     *
     * @param fileName   上传文件的名称
     * @param uploadType 上传文件类型(不同保存的文件夹就不同)
     * @return String
     */
    private String getTrueFileName(String fileName, Integer uploadType) {
        StringBuffer bf = new StringBuffer();

        if (null == uploadType) {
            bf.append("other" + File.separator);
        } else {
            for (Map.Entry<Integer, String> entry : applicationProperties.getFileConfig().getUploadTypeMapping().entrySet()) {
                Integer type = entry.getKey();
                String typeName = entry.getValue();
                if (uploadType.equals(type)) {
                    bf.append(typeName.concat(File.separator));
                    break;
                }
            }
        }
        return bf.append(DateUtils.format(new Date(), applicationProperties.getFileConfig().getUploadPathDataFormat())
                + File.separator + System.currentTimeMillis() + fileName).toString();
    }

    /**
     * 下载文件(后续待优化...有可能是下载模板，或者已经上传的文件)
     *
     * @param fileName 文件路径
     * @param real     是否是绝对路径(如果为True就不转换)
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException
     */
    @RequestMapping("/download")
    public void download(@RequestParam("name") String fileName, Boolean real, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        if (null == fileName) {
            throw new RRException("未找到资源");
        }
        // 默认编码
        String defaultCharsetName = StandardCharsets.UTF_8.name();
        request.setCharacterEncoding(defaultCharsetName);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        // 解码
        fileName = URLDecoder.decode(fileName, defaultCharsetName);
        logger.info("下载文件的名称:{}", fileName);
        if (null == real || !real) {
            fileName = request.getServletContext().getRealPath(fileName);
        }
        logger.info("下载文件的绝对路径" + fileName);
        File file = null;
        try {
            file = new File(fileName);
        } catch (Exception e) {
        }
        if (null != file && file.exists() && file.isFile()) {
            // 获取文件的长度
            long fileLength = file.length();

            // 设置文件输出类型
            try {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                String name = file.getName();
                if (null == real || !real) {
                    name = name.length() > 13 ? name.substring(13) : name;
                }
                response.setHeader("Content-disposition",
                        "attachment; filename=" + URLEncoder.encode(name, defaultCharsetName));
                // 设置输出长度
                response.setHeader("Content-Length", String.valueOf(fileLength));
                // 获取输入流
                bis = new BufferedInputStream(new FileInputStream(file));
                // 输出流
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                // 关闭流
            } catch (Exception e) {
                throw new RRException("下载错误,请重试!");
            } finally {
                if (null != bis) {
                    bis.close();
                }
                if (null != bos) {
                    bos.close();
                }
            }
        } else {
            throw new RRException("未找到资源");
        }
    }
}
