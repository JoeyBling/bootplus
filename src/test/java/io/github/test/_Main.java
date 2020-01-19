package io.github.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.config.filter.MyCorsFilter;
import io.github.util.http.RestTemplateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.http.HttpMethod;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by 思伟 on 2020/1/9
 */
public class _Main {

    public static void main(String[] args) {
        // 不同的平台生成相应平台的换行符
        System.out.println(System.getProperty("line.separator", "\n"));
        System.out.println(MyCorsFilter.class.getSimpleName());
        System.out.println(MyCorsFilter.class.getName());
        System.out.println(MyCorsFilter.class.getTypeName());
        String[] array = ArrayUtils.toArray(
                HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name());
        System.out.println(StringUtils.join(array, ", "));
        System.out.println(ArrayUtils.toString(array
                , StringUtils.EMPTY));
        System.out.println(new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringBuilder.getDefaultStyle()).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.MULTI_LINE_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.SHORT_PREFIX_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.NO_CLASS_NAME_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.NO_FIELD_NAMES_STYLE).append(array).toString());
        System.out.println(RestTemplateUtil.postForObject("https://www.gerensuodeshui.cn/", null, String.class, 2000));


        // Spring原生线程池【写简书】
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        singleThreadPool.shutdown();
    }

}
