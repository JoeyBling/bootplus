package io.github.frame.prj.freemaker.impl;

import freemarker.template.TemplateModelException;
import io.github.frame.prj.freemaker.SimpleTemplateMethodModelEx;
import io.github.util.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 自定义方法格式化时间戳为日期
 * 使用:${formatTimePlus("${admin.lastLoginTime?c}")}
 *
 * @author Created by 思伟 on 2020/8/28
 */
@Component
public class FormatTimeMethodTag extends SimpleTemplateMethodModelEx {

    @Override
    public String getTagName() {
        return "formatTimePlus";
    }

    /**
     * @see DateUtils#DATE_TIME_PATTERN
     */
    @Override
    protected Object getValue(List args) throws TemplateModelException {
        if (CollectionUtils.isEmpty(args)) {
            return null;
        }
        return DateUtils.format(DateUtils.getSecondTimeStampDate(Long.valueOf(getStrValue(args.get(0))))
                , DateUtils.DATE_TIME_PATTERN);
    }

}
