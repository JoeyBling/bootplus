package io.github.util.http;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * RestTemplate 工具类
 * dependency:spring-web-4.3.10.RELEASE.jar commons-lang3-3.3.2.jar
 * APPLICATION_FORM_URLENCODED 类型必须使用LinkedMultiValueMap入参
 *
 * @author Created by 思伟 on 2019/10/22
 * {@link StringHttpMessageConverter#readInternal(Class, HttpInputMessage)}
 */
public class RestTemplateUtil {

    /**
     * 默认本地执行线程
     */
    private static ThreadLocal<RestTemplate> TL_RT = new NamedThreadLocal<RestTemplate>("ThreadLocal_RestTemplate") {
        @Override
        protected RestTemplate initialValue() {
            logger.info("ThreadLocal<RestTemplate> initialValue()...");
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(DEFAULT_TIME_OUT);
            requestFactory.setReadTimeout(DEFAULT_TIME_OUT);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            // 设置编码格式
            setRestTemplateEncode(restTemplate);
            return restTemplate;
        }
    };

    /**
     * @see #setRestTemplateEncode(RestTemplate, Charset)
     */
    public static void setRestTemplateEncode(RestTemplate restTemplate) {
        setRestTemplateEncode(restTemplate, StandardCharsets.UTF_8);
    }

    /**
     * 动态设置编码格式【restTemplate底层是默认使用ISO-8859-1编码】
     *
     * @see StringHttpMessageConverter
     */
    public static void setRestTemplateEncode(RestTemplate restTemplate, Charset charset) {
        if (null == restTemplate || ObjectUtils.isEmpty(restTemplate.getMessageConverters())) {
            return;
        }
        if (null == charset) {
            charset = StandardCharsets.UTF_8;
        }
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (int i = 0; i < messageConverters.size(); i++) {
            HttpMessageConverter<?> httpMessageConverter = messageConverters.get(i);
            if (httpMessageConverter.getClass().equals(StringHttpMessageConverter.class)) {
                messageConverters.set(i, new StringHttpMessageConverter(charset));
            }
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

    /**
     * 默认Http校验正则表达式
     */
    public static final String HTTP_URL_REGEX = "http(s)?:\\/\\/([\\w-]+(\\.|:)?)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?";

    /**
     * Get方式发送请求的标识
     */
    public final static String GET_REQ_CHAR = "?";

    /**
     * Get方式参数拼接符
     */
    public final static String GET_JOIN_CHAR = "=";

    /**
     * Get方式参数分割符
     */
    public final static String GET_SPLIT_CHAR = "&";

    /**
     * 默认超时时间(8S)
     */
    private static int DEFAULT_TIME_OUT = 8 * 1000;

    /**
     * 默认内容编码类型
     */
    private static MediaType DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    /**
     * 默认返回类型
     */
    private static MediaType[] DEFAULT_ACCEPT = new MediaType[]{MediaType.APPLICATION_JSON_UTF8};

    /**
     * 复杂JSON转义正则
     */
    private final static Map<String, String> JSON_ESCAPE_REGEX_MAP;

    /**
     * @see #escapeJsonString(String, Integer)
     */
    static {
        JSON_ESCAPE_REGEX_MAP = Maps.newConcurrentMap();
        JSON_ESCAPE_REGEX_MAP.put("\"\\s*\\{", "{");
        JSON_ESCAPE_REGEX_MAP.put("\\}\\s*\"", "}");
        JSON_ESCAPE_REGEX_MAP.put("\"\\s*\\[", "[");
        JSON_ESCAPE_REGEX_MAP.put("\\]\\s*\"", "]");
        JSON_ESCAPE_REGEX_MAP.put("\\\\\"", "\"");
        JSON_ESCAPE_REGEX_MAP.put("\\\\n", "");
        JSON_ESCAPE_REGEX_MAP.put("\\\\r", "");
    }

    /**
     * 使用postForObject请求接口
     *
     * @param url         URL地址
     * @param body        http参数内容
     * @param contentType 内容编码类型
     * @param t           返回类型
     * @param timeOut     超时时间
     * @param httpHeads   自定义Http头
     * @param charset     编码格式
     * @param accept      客户端可以处理的内容类型
     * @param <T>         返回类型
     * @return
     * @throws RestClientException
     */
    public static <T> T postForObject(final String url, final Object body, final MediaType contentType,
                                      final Class<T> t, final Integer timeOut, final Map<String, String> httpHeads,
                                      Charset charset, final MediaType... accept) throws RestClientException {
        preRequestHandle(url, timeOut, charset);
        Object postBody = buildPostBody(body, contentType);
        HttpEntity<?> formEntity = generateHttpEntity(postBody, contentType, accept, httpHeads);
        logger.info("统一post请求接口：url={},body={},contentType={},httpHeads={}", url, postBody, contentType, httpHeads);
        return TL_RT.get().postForObject(url, formEntity, t);
    }

    /**
     * 预处理POST方式入参
     *
     * @param body        http参数内容
     * @param contentType 内容编码类型
     * @param <T>         返回类型
     * @return post入参
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T buildPostBody(final T body, final MediaType contentType) {
        if (null == body) {
            return null;
        }
        /**
         * @see org.springframework.http.converter.FormHttpMessageConverter#canWrite(Class, MediaType)
         */
        if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
            // APPLICATION_FORM_URLENCODED 类型必须使用LinkedMultiValueMap入参
            if (body instanceof Map && !(body instanceof MultiValueMap)) {
                return (T) mapToLinkedMap((Map) body);
            }
        }
        return body;
    }

    /**
     * Map转LinkedMultiValueMap
     *
     * @param map Map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> LinkedMultiValueMap<K, V> mapToLinkedMap(final Map<K, V> map) {
        LinkedMultiValueMap<K, V> linkedMaps = new LinkedMultiValueMap<K, V>();
        if (!ObjectUtils.isEmpty(map)) {
            Set<Map.Entry<K, V>> entrySet = map.entrySet();
            for (Map.Entry<K, V> stringEntry : entrySet) {
                linkedMaps.add(stringEntry.getKey(), stringEntry.getValue());
            }
        }
        return linkedMaps;
    }

    /**
     * 前置请求处理
     *
     * @param url     URL地址
     * @param timeOut 超时时间
     * @param charset 编码格式
     */
    private static void preRequestHandle(String url, Integer timeOut, Charset charset) {
        Assert.isTrue(isNotHttpUri(url), "非法URI");
        if (null != timeOut) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(timeOut);
            requestFactory.setReadTimeout(timeOut);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            setRestTemplateEncode(restTemplate, charset);
            TL_RT.set(restTemplate);
        }
    }

    /**
     * @see #postForObject(String, Object, MediaType, Class, Integer, Map, MediaType...)
     */
    public static <T> T postForObject(final String url, final Object body, final MediaType contentType,
                                      final Class<T> t, final Integer timeOut, final MediaType... accept) throws RestClientException {
        return postForObject(url, body, contentType, t, timeOut, null, accept);
    }

    /**
     * @see #postForObject(String, Object, MediaType, Class, Integer, Map, Charset, MediaType...)
     */
    public static <T> T postForObject(final String url, final Object body, final MediaType contentType,
                                      final Class<T> t, final Integer timeOut, final Map<String, String> httpHeads, final MediaType... accept) throws RestClientException {
        return postForObject(url, body, contentType, t, timeOut, httpHeads, null, accept);
    }

    /**
     * @see #postForObject(String, Object, MediaType, Class, Integer, MediaType...)
     */
    public static <T> T postForObject(final String url, final Object body, final Class<T> t, final Integer timeOut) {
        return postForObject(url, body, DEFAULT_CONTENT_TYPE, t, timeOut, DEFAULT_ACCEPT);
    }

    /**
     * @see #postForObject(String, Object, MediaType, Class, Integer, Map, MediaType...)
     */
    public static <T> T postForObject(final String url, final Object body, final Class<T> t, final Map<String, String> httpHeads) {
        return postForObject(url, body, DEFAULT_CONTENT_TYPE, t, null, httpHeads, DEFAULT_ACCEPT);
    }

    /**
     * @see #postForObject(String, Object, MediaType, Class, Integer, MediaType...)
     */
    public static <T> T postForObject(final String url, final Object body, final Class<T> t) {
        return postForObject(url, body, DEFAULT_CONTENT_TYPE, t, null, DEFAULT_ACCEPT);
    }

    /**
     * @see #postForObject(String, Object, MediaType, Class, Integer, MediaType...)
     */
    public static <T> T postForObject(final String url, final Object body, final MediaType contentType, final Class<T> t) {
        return postForObject(url, body, contentType, t, null, DEFAULT_ACCEPT);
    }

    /**
     * @see #postForObject(String, Object, Class)
     */
    public static String postForObject(final String url, final Object body) {
        return postForObject(url, body, String.class);
    }

    /**
     * @see #postForObject(String, Object, Class, Map)
     */
    public static String postForObject(final String url, final Object body, final Map<String, String> httpHeads) {
        return postForObject(url, body, String.class, httpHeads);
    }

    /**
     * 使用GET方式请求接口
     *
     * @param url         URL地址
     * @param body        http参数内容
     * @param contentType 内容编码类型
     * @param t           返回类型
     * @param timeOut     超时时间
     * @param httpHeads   自定义Http头
     * @param charset     编码格式
     * @param accept      客户端可以处理的内容类型
     * @param <T>         返回类型
     * @return T
     * @throws RestClientException
     */
    public static <T> T getForObject(final String url, final Object body, final MediaType contentType,
                                     final Class<T> t, final Integer timeOut, final Map<String, String> httpHeads, Charset charset, final MediaType... accept) throws RestClientException {
        preRequestHandle(url, timeOut, charset);
        /**
         * Get方式拼接参数URI即可，不能使用body
         * 否则报错：no suitable HttpMessageConverter found for request type
         * [java.util.concurrent.ConcurrentHashMap] and content type [application/x-www-form-urlencoded]
         * @see RestTemplate.HttpEntityRequestCallback#doWithRequest(ClientHttpRequest)
         */
        HttpEntity<?> formEntity = generateHttpEntity(null, contentType, accept, httpHeads);
        String getUrl = generateGetParams(url, body);
        logger.info("统一get请求接口：url={},body={},contentType={},httpHeads={}", getUrl, body, contentType, httpHeads);
        ResponseEntity<T> exchange = TL_RT.get().exchange(getUrl,
                HttpMethod.GET, formEntity, t);
        return exchange.getBody();
    }

    /**
     * GET方式拼接URL参数
     *
     * @param url  URL地址
     * @param body http参数内容
     * @return URL地址
     */
    protected static String generateGetParams(String url, Object body) {
        StringBuffer sb = new StringBuffer(url);
        if (null != body && body instanceof Map) {
            // TODO Get方式暂时只实现了Map类型入参拼接URL，后续可增加String、JSONArray...
            String urlParams = transferParamsByJoin((Map<?, ?>) body, GET_JOIN_CHAR, GET_SPLIT_CHAR);
            logger.info("拼接Get方式请求参数：{}", urlParams);
            // 拼接URI
            if (!StringUtils.contains(url, GET_REQ_CHAR)) {
                sb.append(GET_REQ_CHAR);
            } else {
                if (!StringUtils.endsWith(url, GET_REQ_CHAR)
                        && !StringUtils.endsWith(url, GET_SPLIT_CHAR)) {
                    sb.append(GET_SPLIT_CHAR);
                }
            }
            sb.append(urlParams);
        }
        return sb.toString();
    }

    /**
     * @see #getForObject(String, Object, MediaType, Class, Integer, Map, Charset, MediaType...)
     */
    public static <T> T getForObject(final String url, final Object body, final MediaType contentType,
                                     final Class<T> t, final Integer timeOut, final Map<String, String> httpHeads, final MediaType... accept) throws RestClientException {
        return getForObject(url, body, contentType, t, timeOut, httpHeads, null, accept);
    }

    /**
     * @see #getForObject(String, Object, MediaType, Class, Integer, Map, MediaType...)
     */
    public static <T> T getForObject(final String url, final Object body, final MediaType contentType,
                                     final Class<T> t, final Integer timeOut, final MediaType... accept) throws RestClientException {
        return getForObject(url, body, contentType, t, timeOut, null, accept);
    }

    /**
     * @see #getForObject(String, Object, MediaType, Class, Integer, MediaType...)
     */
    public static <T> T getForObject(final String url, final Object body, final Class<T> t) {
        return getForObject(url, body, MediaType.APPLICATION_JSON_UTF8, t, null, MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * @see #getForObject(String, Object, MediaType, Class, Integer, Map, MediaType...)
     */
    public static <T> T getForObject(final String url, final Object body, final Class<T> t, final Map<String, String> httpHeads) {
        return getForObject(url, body, MediaType.APPLICATION_JSON_UTF8, t, null, httpHeads, MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * @see #getForObject(String, Object, Class)
     */
    public static String getForObject(final String url, final Object body) {
        return getForObject(url, body, String.class);
    }

    /**
     * @see #getForObject(String, Object, Class, Map)
     */
    public static String getForObject(final String url, final Object body, final Map<String, String> httpHeads) {
        return getForObject(url, body, String.class, httpHeads);
    }

    /**
     * 生成Http实体
     *
     * @see #generateHttpEntity(Object, MediaType, MediaType[], Map)
     */
    public static <T> HttpEntity<T> generateHttpEntity(final T body, final MediaType contentType, final MediaType[] accept) {
        return generateHttpEntity(body, contentType, accept, null);
    }

    /**
     * 生成Http实体
     *
     * @param body        http内容
     * @param contentType 内容编码类型
     * @param accept      客户端可以处理的内容类型
     * @param httpHeads   自定义Http头
     * @return HttpEntity
     */
    public static <T> HttpEntity<T> generateHttpEntity(final T body, final MediaType contentType, final MediaType[] accept, final Map<String, String> httpHeads) {
        HttpHeaders headers = new HttpHeaders();
        if (null != contentType) {
            headers.setContentType(contentType);
        }
        if (!ObjectUtils.isEmpty(accept)) {
            headers.setAccept(Arrays.asList(accept));
        }
        if (null != httpHeads) {
            headers.setAll(httpHeads);
        }
        /**
         * 设置超时请参考{@link SimpleClientHttpRequestFactory}
         */
        //headers.setExpires();
        headers.setCacheControl("no-cache");
        headers.setPragma("no-cache");
        HttpEntity<T> formEntity;
        formEntity = new HttpEntity<>(body, headers);
        return formEntity;
    }

    /**
     * 拼接URL字符串(同时会转换\或/出现2次以上的转/)推荐使用
     *
     * @param url URL字符串
     * @return 拼接后的URL字符串
     */
    public static final String generateHttpUrl(final String... url) {
        if (null == url) {
            return null;
        }
//        org.apache.commons.lang3.StringUtils.trimToEmpty(url);
        //ArrayUtils.nullToEmpty(url);

        // StringUtils.join自动过滤Null值
        String uri = StringUtils.join(url, "/");
        // (?i)在前面 不区分大小写
        // ((ht|f)tp(s?)\:)?
        return uri.replaceAll("(\\\\|/){2,}", "/")
                .replaceFirst("(?i)((ht|f)tp\\:(\\\\|/)+)", "http://")
                .replaceFirst("(?i)((ht|f)tps\\:(\\\\|/)+)", "https://");
    }

    /**
     * 拼接文件路径(同时会转换\或/出现2次以上的转/)推荐使用
     *
     * @param url 文件路径
     * @return 拼接后的文件路径
     */
    public static final String generateFileUrl(final String... url) {
        if (null == url) {
            return null;
        }
        // StringUtils.join自动过滤Null值
        String uri = StringUtils.join(url, StringUtils.equals(
                File.separator, "\\") ? "/" : File.separator);
        // (?i)在前面 不区分大小写
        return uri.replaceAll("(\\\\|/){2,}", "/");
    }

    /**
     * Map集合转为自定义连接字符串
     *
     * @param paramsMap Map<?, ?>
     * @param join      连接的字符串
     * @param split     分隔符
     * @return String
     */
    public static final String transferParamsByJoin(Map<?, ?> paramsMap, String join, String split) {
        if (ObjectUtils.isEmpty(paramsMap)) {
            return StringUtils.EMPTY;
        }
        StringBuffer sb = new StringBuffer();
        Set<? extends Map.Entry<?, ?>> entrySet = paramsMap.entrySet();
        // 是否是第一次
        boolean first = true;
        for (Map.Entry<?, ?> entry : entrySet) {
            if (null != entry.getValue()) {
                if (!first) {
                    sb.append(split);
                }
                first = false;
                sb.append(entry.getKey());
                sb.append(join);
                // TODO 如果Value也是Map的话还要进行判断
                sb.append(entry.getValue());
            }
        }
        return sb.toString();
    }

    /**
     * 判断URI是否是Http路径(是返回false，不是返回true)
     *
     * @param uriArray String...
     * @return boolean
     */
    public static final boolean isNotHttpUri(String... uriArray) {
        if (StringUtils.isAnyEmpty(uriArray)) {
            return false;
        }

        for (String uri : uriArray) {
            if (!Pattern.matches(HTTP_URL_REGEX, uri)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断URI是否其中有一个是Http路径
     *
     * @param uriArray String...
     * @return boolean
     */
    public static final boolean isAnyHttpUri(String... uriArray) {
        if (StringUtils.isAnyEmpty(uriArray)) {
            return false;
        }
        for (String uri : uriArray) {
            if (Pattern.matches(HTTP_URL_REGEX, uri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转义复杂JSON->"{}"转为{},\"转","[]"转[]
     *
     * @param jsonString JSON字符串
     * @return String
     */
    public static final String unescapeJsonString(final String jsonString) {
        if (ObjectUtils.isEmpty(JSON_ESCAPE_REGEX_MAP) || StringUtils.isNotBlank(jsonString)) {
            Set<Map.Entry<String, String>> entries = JSON_ESCAPE_REGEX_MAP.entrySet();
            String newJsonString = jsonString;
            for (Map.Entry<String, String> entry : entries) {
                newJsonString = newJsonString.replaceAll(entry.getKey(), entry.getValue());
                /*try {
                    JSON.parseObject(newJsonString);
                } catch (JSONException e) {
                    continue;
                }
                break;*/
            }
            return newJsonString;
        }
        return jsonString;
    }

    /**
     * Sets the value of DEFAULT_TIME_OUT.
     *
     * @param defaultTimeOut DEFAULT_TIME_OUT
     */
    public static void setDefaultTimeOut(int defaultTimeOut) {
        DEFAULT_TIME_OUT = defaultTimeOut;
    }

    /**
     * Sets the value of DEFAULT_CONTENT_TYPE.
     *
     * @param defaultContentType DEFAULT_CONTENT_TYPE
     */
    public static void setDefaultContentType(MediaType defaultContentType) {
        DEFAULT_CONTENT_TYPE = defaultContentType;
    }

    /**
     * Sets the value of DEFAULT_ACCEPT.
     *
     * @param defaultAccept DEFAULT_ACCEPT
     */
    public static void setDefaultAccept(MediaType[] defaultAccept) {
        DEFAULT_ACCEPT = defaultAccept;
    }

    public static int getDefaultTimeOut() {
        return DEFAULT_TIME_OUT;
    }

    public static MediaType getDefaultContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    public static MediaType[] getDefaultAccept() {
        return DEFAULT_ACCEPT;
    }

    public static void main(String[] args) {
        String s = RestTemplateUtil.generateHttpUrl("www.baidu,com\\\\sad//asdas",
                "https://asd\\", "啊是大", "////s///adada", null);
        System.out.println(s);
        s = RestTemplateUtil.generateFileUrl("/C:/Users/Administrator.QH-20150311UHWZ/Desktop/%e5%b9%b3%e5%ae%89%e5%a5%bd%e5%8c%bb%e7%94%9f/server-net/smarthos-recipe/src/main/webapp/WEB-INF/classes/fonts/SIMLI.TTF", null);
        System.out.println(s);
        System.out.println("Users/Administra".toUpperCase(Locale.ENGLISH));
        System.out.println(RestTemplateUtil.isNotHttpUri("http://as.cn", "https://www.baid" +
                "u.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=java%E4%BD%BF%E7" +
                "%94%A8%E6%AD%A3%E5%88%99&oq=java&rsv_pq=fad4cd380003ba66" +
                "&rsv_t=27d9KW2GQLczi2HTfKg61VnHxi3ZRza5TbAxW6c9gNCotxve%2F8Z" +
                "UtDHbu58&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=31&rsv_sug1=19&" +
                "rsv_sug7=100&rsv_sug2=0&inputT=4444&rsv_sug4=4444"));
        String url = "http11://yingyan.baidu.com/api/v3";
        System.out.println(
                RestTemplateUtil.isNotHttpUri(url));
        Assert.isTrue(isNotHttpUri(url), "非法URI");
    }

}
