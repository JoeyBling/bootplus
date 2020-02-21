package io.github.util.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Apache-HttpClient实现
 *
 * @author Created by 思伟 on 2020/1/15
 */
public class ApacheHttpClient {

    /**
     * 默认编码-UTF-8
     */
    public final static String DEFAULT_CHARSET_UTF_8 = StandardCharsets.UTF_8.name();

    /**
     * 请求方法
     */
    private static final String _GET = "GET";
    private static final String _POST = "POST";
    private static final String _PUT = "PUT";
    private static final String _DELETE = "DELETE";

    /**
     * @see #getContentType(String, String, String, String)
     */
    public static String getContentType(final String url, final String httpRequestBody) throws IOException {
        return getContentType(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * GET方式获取响应对象的ContentType
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return 响应对象的ContentType
     * @throws IOException
     */
    public static String getContentType(final String url, final String httpRequestBody,
                                        final String charset, final String contentType) throws IOException {
        Header headerType = getHttpEntityByGet(url, httpRequestBody, charset, contentType).getContentType();
        if (null == headerType) {
            return null;
        }
        return headerType.getValue();
    }

    /**
     * @see #getHttpEntityByGet(String, String, String, String)
     */
    public static HttpEntity getHttpEntityByGet(final String url, final String httpRequestBody) throws IOException {
        return getHttpEntityByGet(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * GET方式获取HttpEntity体
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return HttpEntity
     * @throws IOException
     */
    public static HttpEntity getHttpEntityByGet(final String url, final String httpRequestBody,
                                                final String charset, final String contentType) throws IOException {
        try (CloseableHttpResponse response = getResponse(getHttpUriRequest(url, _GET),
                generateEntity(httpRequestBody, charset, contentType))) {
            HttpEntity entity = response.getEntity();
            return entity;
        }
    }

    /**
     * @see #getHttpEntityByPost(String, String, String, String)
     */
    public static HttpEntity getHttpEntityByPost(final String url, final String httpRequestBody) throws IOException {
        return getHttpEntityByPost(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * POST方式获取HttpEntity体
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return HttpEntity
     * @throws IOException
     */
    public static HttpEntity getHttpEntityByPost(final String url, final String httpRequestBody,
                                                 final String charset, final String contentType) throws IOException {
        try (CloseableHttpResponse response = getResponse(getHttpUriRequest(url, _POST),
                generateEntity(httpRequestBody, charset, contentType))) {
            HttpEntity entity = response.getEntity();
            return entity;
        }
    }

    /**
     * @see #httpPost(String, String, String, String)
     */
    public static String httpPost(final String url, final String httpRequestBody) throws IOException {
        return httpPost(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * POST方法请求
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return String
     * @throws IOException
     */
    public static String httpPost(final String url, final String httpRequestBody,
                                  final String charset, final String contentType) throws IOException {
        try (CloseableHttpResponse response = getResponse(getHttpUriRequest(url, _POST),
                generateEntity(httpRequestBody, charset, contentType))) {
            return getString(charset, response);
        }
    }

    /**
     * @see #httpGet(String, String, String, String)
     */
    public static String httpGet(final String url, final String httpRequestBody) throws IOException {
        return httpGet(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * GET方法请求
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return String
     * @throws IOException
     */
    public static String httpGet(final String url, final String httpRequestBody,
                                 final String charset, final String contentType) throws IOException {
        try (CloseableHttpResponse response = getResponse(getHttpUriRequest(url, _GET),
                generateEntity(httpRequestBody, charset, contentType))) {
            return getString(charset, response);
        }
    }

    /**
     * @see #httpPut(String, String, String, String)
     */
    public static String httpPut(final String url, final String httpRequestBody) throws IOException {
        return httpPut(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * PUT方法请求
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return String
     * @throws IOException
     */
    public static String httpPut(final String url, final String httpRequestBody,
                                 final String charset, final String contentType) throws IOException {
        try (CloseableHttpResponse response = getResponse(getHttpUriRequest(url, _PUT),
                generateEntity(httpRequestBody, charset, contentType))) {
            return getString(charset, response);
        }
    }

    /**
     * @see #httpDelete(String, String, String, String)
     */
    public static String httpDelete(final String url, final String httpRequestBody) throws IOException {
        return httpDelete(url, httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * DELETE方法请求
     *
     * @param url             Http请求地址
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return String
     * @throws IOException
     */
    public static String httpDelete(final String url, final String httpRequestBody,
                                    final String charset, final String contentType) throws IOException {
        try (CloseableHttpResponse response = getResponse(getHttpUriRequest(url, _DELETE),
                generateEntity(httpRequestBody, charset, contentType))) {
            return getString(charset, response);
        }
    }

    /**
     * 获取返回字符串
     *
     * @param charset  编码
     * @param response 响应对象
     * @return String
     * @throws IOException
     */
    private static String getString(String charset, CloseableHttpResponse response) throws IOException {
        try (InputStream in = response.getEntity().getContent()) {
            String responseString = IOUtils.toString(in, charset);
            return responseString;
        }
    }

    /**
     * 根据提交方式名获取请求对象
     *
     * @param uri        Http请求地址
     * @param httpMethod Http请求方法
     * @return HttpUriRequest
     */
    private static HttpUriRequest getHttpUriRequest(String uri, String httpMethod) {
        HttpUriRequest request;
        // 默认以post处理
        if (StringUtils.isBlank(httpMethod)) {
            request = new HttpPost(uri);
        } else if (_GET.equalsIgnoreCase(httpMethod)) {
            request = new HttpGet(uri);
        } else if (_POST.equalsIgnoreCase(httpMethod)) {
            request = new HttpPost(uri);
        } else if (_PUT.equalsIgnoreCase(httpMethod)) {
            request = new HttpPut(uri);
        } else if (_DELETE.equalsIgnoreCase(httpMethod)) {
            request = new HttpDelete(uri);
        } else {
            request = new HttpPost(uri);
        }
        return request;
    }


    /**
     * 生成HttpClient请求实体
     *
     * @param httpRequestBody 请求体
     * @param charset         编码
     * @param contentType     内容类型标头
     * @return HttpEntity
     */
    private static HttpEntity generateEntity(
            final Object httpRequestBody, final String charset, final String contentType) {
        Assert.notNull(charset, "Charset must not be null");
        if (null != httpRequestBody) {
            // 不同的类型生成不同的Entity，暂时只实现一种String
            if (httpRequestBody instanceof String) {
                // ContentType默认为TEXT_PLAIN
                StringEntity stringEntity = new StringEntity((String) httpRequestBody, charset);
                stringEntity.setContentEncoding(charset);
                if (null != contentType) {
                    stringEntity.setContentType(contentType);
                }
                return stringEntity;
            }
        }
        return null;
    }

    /**
     * @see #generateEntity(Object, String, String)
     */
    private static HttpEntity generateEntity(
            final Object httpRequestBody) {
        return generateEntity(httpRequestBody, DEFAULT_CHARSET_UTF_8, null);
    }

    /**
     * @see #generateEntity(Object, String, String)
     */
    private static HttpEntity generateEntity(
            final Object httpRequestBody, final String contentType) {
        return generateEntity(httpRequestBody, DEFAULT_CHARSET_UTF_8, contentType);
    }

    /**
     * 发起ApacheHttpClient请求
     *
     * @param request 请求对象
     * @param entity  请求实体
     * @param <T>     HttpUriRequest
     * @return CloseableHttpResponse
     * @throws IOException
     */
    private static <T extends HttpUriRequest> CloseableHttpResponse getResponse(
            final T request, final HttpEntity entity)
            throws IOException {
        Assert.notNull(request, "HttpUriRequest must not be null");
        CloseableHttpResponse response = null;
        try {
            // 这个也不能过早关闭，否则一样有问题
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // Http认证授权
//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "*****");
//            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
//            HttpClientContext context = HttpClientContext.create();
//            context.setCredentialsProvider(credentialsProvider);

            if (null != entity) {
                if (request instanceof HttpPost) {
                    ((HttpPost) request).setEntity(entity);
                } else if (request instanceof HttpPut) {
                    ((HttpPut) request).setEntity(entity);
                }
            }
//            response = httpClient.execute(request, context);
            response = httpClient.execute(request);
            return response;
        } catch (IOException e) {
            throw e;
        } finally {
            // 现在返回了Response，由调用者关闭
            /*try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }*/
        }
    }

    /**
     * @see #getResponse(HttpUriRequest, HttpEntity)
     */
    private static <T extends HttpUriRequest> CloseableHttpResponse getResponse(
            final T request) throws IOException {
        return getResponse(request, null);
    }

    /**
     * Test & Verify
     */
    public static void main(String[] args) throws IOException {
        // text/html;charset=utf-8
        String url = "https://www.baidu.com/";
//        url = "http://gogs-git.hztywl.cn/";
//        url = "http://183.129.141.106:8081/nexus/content/groups/public/com/tynet/";
        for (int i = 0; i < 2; i++) {
            System.out.println(ApacheHttpClient.getContentType(url, null));
            System.out.println(ApacheHttpClient.httpGet(url, null));
        }
    }

}
