/*
 * 微信公众平台(JAVA) SDK
 *
 * Copyright (c) 2014, Ansitech Network Technology Co.,Ltd All rights reserved.
 *
 * http://www.weixin4j.org/sdk/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.util.http;

import com.alibaba.fastjson.JSONObject;
import io.github.util.exception.SysRuntimeException;
import io.github.util.encry.Md5Util;
import io.github.util.file.FileTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 请求微信平台及响应的客户端类
 *
 * <p>
 * 每一次请求即对应一个<tt>HttpsClient</tt>，
 * 每次登陆产生一个<tt>OAuth</tt>用户连接,使用<tt>OAuthToken</tt>
 * 可以不用重复向微信平台发送登陆请求，在没有过期时间内，可继续请求。</p>
 *
 * @author Updated by 思伟 on 2020/1/8
 */
public class HttpsClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpsClient.class);

    /**
     * OK: Success!
     */
    private static final int OK = 200;
    private static final int CONNECTION_TIMEOUT = 25000;
    private static final int READ_TIMEOUT = 25000;
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";
    private static final String DELETE_METHOD = "DELETE";

    public HttpsClient() {
    }

    /**
     * Post JSON数据
     * <p>
     * 默认https方式
     *
     * @param url  提交地址
     * @param json JSON数据
     * @return 输出流对象
     * @throws SysRuntimeException
     */
    public HttpResponse post(String url, JSONObject json) throws SysRuntimeException {
        //将JSON数据转换为String字符串
        String jsonString = json == null ? null : json.toString();
        //提交数据
        return httpsRequest(url, POST_METHOD, jsonString, false, null, null, null, null);
    }

    public HttpResponse post(String url, String json) throws SysRuntimeException {
        //将JSON数据转换为String字符串
        //提交数据
        return httpsRequest(url, POST_METHOD, json, false, null, null, null, null);
    }

    public HttpResponse post(String url, String json, Map<String, String> headerProperties) throws SysRuntimeException {
        //将JSON数据转换为String字符串
        //提交数据
        return httpsRequest(url, POST_METHOD, json, false, null, null, null, headerProperties);
    }

    /**
     * Put JSON数据
     * <p>
     * 默认https方式
     *
     * @param url  提交地址
     * @param body JSON数据
     * @return 输出流对象
     * @throws SysRuntimeException
     */
    public HttpResponse put(String url, String body) throws SysRuntimeException {
        //将JSON数据转换为String字符串
        //提交数据
        return httpsRequest(url, PUT_METHOD, body, false, null, null, null, null);
    }

    public HttpResponse put(String url, String json, Map<String, String> headerProperties) throws SysRuntimeException {
        //将JSON数据转换为String字符串
        //提交数据
        return httpsRequest(url, PUT_METHOD, json, false, null, null, null, headerProperties);
    }

    /**
     * 默认https方式
     *
     * @param url 提交地址
     * @return 输出流对象
     * @throws SysRuntimeException
     */
    public HttpResponse delete(String url) throws SysRuntimeException {
        //提交数据
        return httpsRequest(url, DELETE_METHOD, null, false, null, null, null, null);
    }

    public HttpResponse delete(String url, Map<String, String> headerProperties) throws SysRuntimeException {
        //提交数据
        return httpsRequest(url, DELETE_METHOD, null, false, null, null, null, headerProperties);
    }

    /**
     * Get 请求
     * <p>
     * 默认https方式
     *
     * @param url 请求地址
     * @return 输出流对象
     * @throws SysRuntimeException
     */
    public HttpResponse get(String url) throws SysRuntimeException {
        return httpsRequest(url, GET_METHOD, null, false, null, null, null, null);
    }

    public HttpResponse get(String url, Map<String, String> headerProperties) throws SysRuntimeException {
        return httpsRequest(url, GET_METHOD, null, false, null, null, null, headerProperties);
    }

    /**
     * Post XML格式数据
     *
     * @param url 提交地址
     * @param xml XML数据
     * @return 输出流对象
     * @throws SysRuntimeException
     */
    public HttpResponse postXml(String url, String xml) throws SysRuntimeException {
        return httpsRequest(url, POST_METHOD, xml, false, null, null, null, null);
    }

    /**
     * Post XML格式数据
     *
     * @param url 提交地址
     * @param xml XML数据
     * @param needCert 需要证书
     * @return 输出流对象
     * @throws SysRuntimeException
     */
//    public HttpResponse postXml(String url, String xml, boolean needCert) throws SysRuntimeException {
//        String partnerId = Configuration.getProperty("weixin4j.pay.partner.id");
//        String certPath = Configuration.getProperty("weixin4j.http.cert.path");
//        String certSecret = Configuration.getProperty("weixin4j.http.cert.secret");
//        return httpsRequest(url, POST_METHOD, xml, needCert, partnerId, certPath, certSecret);
//    }

    /**
     * Post XML格式数据
     *
     * @param url        提交地址
     * @param xml        XML数据
     * @param partnerId  商户ID
     * @param certPath   证书地址
     * @param certSecret 证书密钥
     * @return 输出流对象
     * @throws SysRuntimeException
     */
    public HttpResponse postXml(String url, String xml, String partnerId, String certPath, String certSecret, Map<String, String> headerProperties) throws SysRuntimeException {
        return httpsRequest(url, POST_METHOD, xml, true, partnerId, certPath, certSecret, headerProperties);
    }

    /**
     * 通过https协议请求url
     *
     * @param url      提交地址
     * @param method   提交方式
     * @param postData 提交数据
     * @return 响应流
     * @throws SysRuntimeException
     */
    private HttpResponse httpsRequest(String url, String method, String postData, boolean needCert, String partnerId, String certPath, String certSecret, Map<String, String> headerProperties)
            throws SysRuntimeException {
        HttpResponse res = null;
        HttpsURLConnection https;
        try {
            logger.debug(String.format("%s %s", method, url));
            //创建https请求连接
            https = getHttpsUrlConnection(url);
            //判断https是否为空，如果为空返回null响应
            if (https != null) {
                //设置Header信息，包括https证书
                setHttpsHeader(https, method, needCert, partnerId, certPath, certSecret, headerProperties);
                //判断是否需要提交数据
                if (!method.equals(GET_METHOD) && null != postData) {
                    logger.debug(String.format("body %s", postData));
                    //讲参数转换为字节提交
                    byte[] bytes = postData.getBytes(DEFAULT_CHARSET);
                    //设置头信息
                    https.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                    //开始连接
                    https.connect();
                    //获取返回信息
                    try (OutputStream output = https.getOutputStream()) {
                        output.write(bytes);
                        output.flush();
                    }
                } else {
                    //开始连接
                    https.connect();
                }
                //创建输出对象
                res = new HttpResponse(https);
                //获取响应代码
                if (res.getStatus() == OK) {
                    return res;
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (KeyManagementException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (NoSuchProviderException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (KeyStoreException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (CertificateException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (UnrecoverableKeyException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        }
        return res;
    }

    /**
     * 获取https请求连接
     *
     * @param url 连接地址
     * @return https连接对象
     * @throws IOException
     */
    private HttpsURLConnection getHttpsUrlConnection(String url) throws IOException {
        URL urlGet = new URL(url);
        //创建https请求
        HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlGet.openConnection();
        return httpsUrlConnection;
    }

    private void setHttpsHeader(HttpsURLConnection httpsUrlConnection, String method, boolean needCert, String partnerId, String certPath, String certSecret, Map<String, String> headerProperties)
            throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException,
            IOException, KeyStoreException, CertificateException, UnrecoverableKeyException {
        //不需要维修证书，则使用默认证书
        if (!needCert) {
            //创建https请求证书
            TrustManager[] tm = {new MyX509TrustManager()};
            //创建证书上下文对象
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            //初始化证书信息
            sslContext.init(null, tm, new SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            //设置ssl证书
            httpsUrlConnection.setSSLSocketFactory(ssf);
        } else {
            //指定读取证书格式为PKCS12
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //读取本机存放的PKCS12证书文件
            try (FileInputStream instream = new FileInputStream(new File(certPath))) {
                //指定PKCS12的密码
                keyStore.load(instream, partnerId.toCharArray());
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, certSecret.toCharArray());
            //创建管理jks密钥库的x509密钥管理器，用来管理密钥，需要key的密码  
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            // 构造SSL环境，指定SSL版本为3.0，也可以使用TLSv1，但是SSLv3更加常用。  
            sslContext.init(kmf.getKeyManagers(), null, null);
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            //设置ssl证书
            httpsUrlConnection.setSSLSocketFactory(ssf);
        }
        if (null != headerProperties) {
            Iterator<String> it = headerProperties.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                logger.debug(String.format("header %s:%s", key, headerProperties.get(key)));
                httpsUrlConnection.setRequestProperty(key, headerProperties.get(key));
            }
        }
        //设置header信息
//        httpsUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //设置User-Agent信息
//        httpsUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");


        // 设置可接受信息
        httpsUrlConnection.setDoOutput(true);
        // 设置可输入信息
        httpsUrlConnection.setDoInput(true);
        // 设置请求方式
        httpsUrlConnection.setRequestMethod(method);
        // 设置连接超时时间
        httpsUrlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        // 设置请求超时
        httpsUrlConnection.setReadTimeout(READ_TIMEOUT);
        // 设置编码
        httpsUrlConnection.setRequestProperty("Charsert", StandardCharsets.UTF_8.name());
    }

    /**
     * 上传文件
     *
     * @param url  上传地址
     * @param file 上传文件对象
     * @return 服务器上传响应结果
     */
    public String uploadHttps(String url, File file) throws SysRuntimeException {
        HttpsURLConnection https = null;
        StringBuffer bufferRes = new StringBuffer();
        try {
            // 定义数据分隔线 
            String boundary = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL";
            //创建https请求连接
            https = getHttpsUrlConnection(url);
            //设置header和ssl证书
            setHttpsHeader(https, POST_METHOD, false, null, null, null, null);
            //不缓存
            https.setUseCaches(false);
            //保持连接
            https.setRequestProperty("connection", "Keep-Alive");
            //设置文档类型
            https.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            //定义输出流
            OutputStream out = null;
            try {
                out = new DataOutputStream(https.getOutputStream());
                byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();// 定义最后数据分隔线
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
                    out.write("\r\n".getBytes()); //多个文件时，二个文件之间加入这个
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
                ins = https.getInputStream();
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
        } catch (IOException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (KeyManagementException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (NoSuchProviderException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (KeyStoreException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (CertificateException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (UnrecoverableKeyException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } finally {
            if (https != null) {
                // 关闭连接
                https.disconnect();
            }
        }
        return bufferRes.toString();
    }

    /**
     * 下载附件
     *
     * @param url 附件地址
     * @return 附件对象
     * @throws SysRuntimeException
     */
    public HttpAttachment downloadHttps(String url) throws SysRuntimeException {
        //定义下载附件对象
        HttpAttachment attachment = null;
        HttpsURLConnection https;
        try {
            //创建https请求连接
            https = getHttpsUrlConnection(url);
            //设置header和ssl证书
            setHttpsHeader(https, POST_METHOD, false, null, null, null, null);
            //不缓存
            https.setUseCaches(false);
            //保持连接
            https.setRequestProperty("connection", "Keep-Alive");

            //初始化返回附件对象
            attachment = new HttpAttachment();
            //根据下载响应内容创建输出流
            if (StringUtils.containsAny(https.getContentType(), MediaType.TEXT_PLAIN_VALUE)
                    || StringUtils.containsAny(https.getContentType(), "application/json")) {
                // 定义BufferedReader输入流来读取URL的响应  
                StringBuilder bufferRes;
                try (InputStream in = https.getInputStream();
                     BufferedReader read = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()))) {
                    String valueString;
                    bufferRes = new StringBuilder();
                    while ((valueString = read.readLine()) != null) {
                        bufferRes.append(valueString);
                    }
                }
                attachment.setError(bufferRes.toString());
            } else {
                BufferedInputStream bis = new BufferedInputStream(https.getInputStream());
                String ds = https.getHeaderField("Content-disposition");
                String fullName = "", relName = "", suffix = "";
                String filename = "filename";
                if (StringUtils.isNotBlank(ds) && StringUtils.containsAny(ds, filename)) {
                    // 从http头获取文件名称等信息
                    fullName = ds.substring(ds.indexOf("filename=\"") + 10, ds.length() - 1);
                    relName = fullName.substring(0, fullName.lastIndexOf("."));
                    suffix = fullName.substring(relName.length() + 1);

                } else {
                    // http头中不含文件信息
                    relName = Md5Util.encode(System.currentTimeMillis() + UUID.randomUUID().toString()); // 随机生成文件名
                    suffix = FileTypeUtil.getExtensionNameByContentType(https.getHeaderField("Content-Type")).substring(1); // 获取扩展名，不含点
                    fullName = relName + "." + suffix;
                }

                attachment.setFullName(fullName);
                attachment.setFileName(relName);
                attachment.setSuffix(suffix);
                attachment.setContentLength(https.getHeaderField("Content-Length"));
                attachment.setContentType(https.getHeaderField("Content-Type"));

                attachment.setFileStream(bis);
            }
        } catch (IOException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (KeyManagementException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (NoSuchProviderException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (KeyStoreException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (CertificateException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } catch (UnrecoverableKeyException ex) {
            throw new SysRuntimeException(ex.getMessage(), ex);
        } finally {
        }
        return attachment;
    }
}
