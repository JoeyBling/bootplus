package io.github.test;

import io.github.config.filter.MyCorsFilter;
import io.github.util.http.RestTemplateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpMethod;

/**
 * @author Created by 思伟 on 2020/1/9
 */
public class _Main {

    public static void main(String[] args) {
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
    }

}
