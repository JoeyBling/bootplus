package io.github.util.http;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import io.github.util.encry.Md5Util;
import io.github.util.file.FileTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * HttpClient业务
 *
 * @author Created by 思伟 on 2020/1/8
 */
public class HttpClient {

    /**
     * OK: Success!
     */
    private static final int OK = 200;
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 3000;
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private String contentType = "application/x-www-form-urlencoded";

    /**
     * Get 请求
     *
     * @param url 请求地址
     * @return 输出流对象
     */
    public HttpResponse get(String url) throws IOException {
        return httpRequest(url, GET_METHOD, "", DEFAULT_CHARSET);
    }

    /**
     * Post 请求
     *
     * @param url 请求地址
     * @return 输出流对象
     * @throws IOException
     */
    public HttpResponse post(String url, String postData, String charset) throws IOException {
        return httpRequest(url, POST_METHOD, postData, charset);
    }

    public HttpResponse post(String url, String postData) throws IOException {
        return httpRequest(url, POST_METHOD, postData, DEFAULT_CHARSET);
    }

    public HttpResponse post(String url, Map<String, String> postParams, String charset) throws IOException {
        return httpRequest(url, POST_METHOD, postParams, charset);
    }

    public HttpResponse post(String url, Map<String, String> postParams) throws IOException {
        return httpRequest(url, POST_METHOD, postParams, DEFAULT_CHARSET);
    }

    /**
     * 上传文件
     *
     * @param url  上传地址
     * @param file 上传文件对象
     * @return 服务器上传响应结果
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     */
    public String upload(String url, File file) throws IOException,
            NoSuchAlgorithmException, NoSuchProviderException,
            KeyManagementException {
        HttpURLConnection http = null;
        StringBuffer bufferRes = new StringBuffer();
        try {
            // 定义数据分隔线 
            String boundary = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL";
            //创建https请求连接
            http = getHttpUrlConnection(url);
            //设置header和ssl证书
            setHttpHeader(http, POST_METHOD);
            //不缓存
            http.setUseCaches(false);
            //保持连接
            http.setRequestProperty("connection", "Keep-Alive");
            //设置文档类型
            http.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            //定义输出流
            OutputStream out = null;
            try {
                out = new DataOutputStream(http.getOutputStream());
                // 定义最后数据分隔线
                byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(boundary);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"media\";filename=\"").append(file.getName()).append("\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);
                //读取文件流
                try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file))) {
                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = dataInputStream.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    //多个文件时，二个文件之间加入这个
                    out.write("\r\n".getBytes());
                }
                out.write(endData);
                out.flush();
            } finally {
                if (out != null) {
                    out.close();
                }
            }

            // 定义BufferedReader输入流来读取URL的响应  
            InputStream ins = null;
            try {
                ins = http.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
                String valueString;
                bufferRes = new StringBuffer();
                while ((valueString = read.readLine()) != null) {
                    bufferRes.append(valueString);
                }
            } finally {
                if (ins != null) {
                    ins.close();
                }
            }
        } finally {
            if (http != null) {
                // 关闭连接
                http.disconnect();
            }
        }
        return bufferRes.toString();
    }

    /**
     * 下载附件
     *
     * @param httpUrl 附件地址
     * @return 附件对象
     * @throws IOException
     */
    public HttpAttachment download(String httpUrl) throws IOException, NoSuchAlgorithmException {
        HttpAttachment att = new HttpAttachment();
        URL url = new URL(httpUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        //设置头
        setHttpHeader(http, "GET");
        if (StringUtils.equalsIgnoreCase(http.getContentType(), MediaType.TEXT_PLAIN_VALUE)) {
            // 定义BufferedReader输入流来读取URL的响应  
            StringBuilder bufferRes;
            InputStream in = null;
            try {
                in = http.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
                String valueString;
                bufferRes = new StringBuilder();
                while ((valueString = read.readLine()) != null) {
                    bufferRes.append(valueString);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
            }
            att.setError(bufferRes.toString());
        } else {
            BufferedInputStream bis = new BufferedInputStream(http.getInputStream());
            String ds = http.getHeaderField("Content-disposition");
            String fullName = "", relName = "", suffix = "";
            String filename = "filename";
            if (StringUtils.isNotBlank(ds) && StringUtils.containsAny(ds, filename)) {
                // 从http头获取文件名称等信息
                fullName = ds.substring(ds.indexOf("filename=\"") + 10, ds.length() - 1);
                relName = fullName.substring(0, fullName.lastIndexOf("."));
                suffix = fullName.substring(relName.length() + 1);

            } else {
                // http头中不含文件信息
                // 随机生成文件名
                relName = Md5Util.encode(System.currentTimeMillis() + UUID.randomUUID().toString());
                // 获取扩展名，不含点
                suffix = FileTypeUtil.getExtensionNameByContentType(http.getHeaderField("Content-Type")).substring(1);
                fullName = relName + "." + suffix;
            }
            att.setFullName(fullName);
            att.setFileName(relName);
            att.setSuffix(suffix);
            att.setContentLength(http.getHeaderField("Content-Length"));
            att.setContentType(http.getHeaderField("Content-Type"));

            att.setFileStream(bis);
        }
        return att;
    }

    /**
     * 获取http请求连接
     *
     * @param url 连接地址
     * @return http连接对象
     * @throws IOException
     */
    private HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        URL urlGet = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlGet.openConnection();
        return con;
    }

    /**
     * 通过http协议请求url
     *
     * @param url      提交地址
     * @param method   提交方式
     * @param postData 提交数据
     * @return 响应流
     * @throws IOException
     */
    private HttpResponse httpRequest(String url, String method, String postData, String charset)
            throws IOException {
        HttpResponse res = null;
        OutputStream output = null;
        HttpURLConnection http;
        //创建https请求连接
        http = getHttpUrlConnection(url);
        //判断https是否为空，如果为空返回null响应
        if (http != null) {
            //设置Header信息
            try {
                JSONObject.parseObject(postData);
                setJsonHttpHeader(http, method);
            } catch (JSONException e) {
                setHttpHeader(http, method);
            }
            //判断是否需要提交数据
            if (method.equals(POST_METHOD) && null != postData && postData.length() > 0) {
                //将参数转换为字节提交
                byte[] bytes = postData.getBytes(charset);
                //设置头信息
                http.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                //开始连接
                http.connect();
                //获取返回信息
                try {
                    output = http.getOutputStream();
                    output.write(bytes);
                    output.flush();
                } finally {
                    if (null != output) {
                        output.close();
                    }
                }
            } else {
                //开始连接
                http.connect();
            }
            //创建输出对象
            res = new HttpResponse(http);
            //获取响应代码
            if (res.getStatus() == OK) {
                return res;
            }
        }
        return res;
    }

    private HttpResponse httpRequest(String url, String method, Map<String, String> postParams, String charset)
            throws IOException {
        HttpResponse res = null;
        OutputStream output = null;
        HttpURLConnection http;
        //创建https请求连接
        http = getHttpUrlConnection(url);
        //判断https是否为空，如果为空返回null响应
        if (http != null) {
            //设置Header信息
            setHttpHeader(http, method);
            //判断是否需要提交数据
            if (method.equals(POST_METHOD) && null != postParams) {
                String postData = "";
                Iterator<Entry<String, String>> it = postParams.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> entry = it.next();
                    postData += String.format("%s=%s&", entry.getKey(), entry.getValue());
                }

                //将参数转换为字节提交
                byte[] bytes = postData.getBytes(charset);
                //设置头信息
                http.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                //开始连接
                http.connect();
                //获取返回信息
                try {
                    output = http.getOutputStream();
                    output.write(bytes);
                    output.flush();
                } finally {
                    if (null != output) {
                        output.close();
                    }
                }
            } else {
                //开始连接
                http.connect();
            }
            //创建输出对象
            res = new HttpResponse(http);
            //获取响应代码
            if (res.getStatus() == OK) {
                return res;
            }
        }
        return res;
    }

    private void setHttpHeader(HttpURLConnection httpUrlConnection, String method)
            throws IOException {
        //设置header信息
        httpUrlConnection.setRequestProperty("Content-Type", contentType);
        //设置User-Agent信息
        httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        //设置可接受信息
        httpUrlConnection.setDoOutput(true);
        //设置可输入信息
        httpUrlConnection.setDoInput(true);
        //设置请求方式
        httpUrlConnection.setRequestMethod(method);
        //设置连接超时时间
        if (CONNECTION_TIMEOUT > 0) {
            httpUrlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        }
        //设置请求超时
        if (READ_TIMEOUT > 0) {
            httpUrlConnection.setReadTimeout(READ_TIMEOUT);
        }
        //设置编码
        httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
    }

    private void setJsonHttpHeader(HttpURLConnection httpUrlConnection, String method)
            throws IOException {
        //设置header信息
        httpUrlConnection.setRequestProperty("Content-Type", "application/json");
        //设置User-Agent信息
        httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        //设置可接受信息
        httpUrlConnection.setDoOutput(true);
        //设置可输入信息
        httpUrlConnection.setDoInput(true);
        //设置请求方式
        httpUrlConnection.setRequestMethod(method);
        //设置连接超时时间
        if (CONNECTION_TIMEOUT > 0) {
            httpUrlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        }
        //设置请求超时
        if (READ_TIMEOUT > 0) {
            httpUrlConnection.setReadTimeout(READ_TIMEOUT);
        }
        //设置编码
        httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
