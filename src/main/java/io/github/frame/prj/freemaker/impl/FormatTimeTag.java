package io.github.frame.prj.freemaker.impl;

import freemarker.template.TemplateModelException;
import io.github.frame.prj.freemaker.SimpleTemplateDirectiveModel;
import io.github.util.DateUtils;
import io.github.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * FreeMarker格式化时间戳为指定日期
 * 使用:[#-- 自定义标签格式化时间戳 ?c防止时间戳有,逗号 --]
 * [@formatTime unix="${entity.createTime?c}" pattern="yyyy-MM-dd HH:mm:ss"] [/@formatTime]
 *
 * @author Created by 思伟 on 2020/6/6
 */
@Component
public class FormatTimeTag extends SimpleTemplateDirectiveModel {
    /**
     * 时间戳参数名
     */
    private final String UNIX_PARAM_NAME = "unix";
    /**
     * 日期格式化格式
     */
    private final String PATTERN_PARAM_NAME = "pattern";

    @Override
    public String getTagName() {
        return "formatTime";
    }

    @Override
    protected boolean requireBody() {
        return false;
    }

    @Override
    protected void render(Writer out, Map params) throws TemplateModelException, IOException {
        final String unix = getStrParam(params, UNIX_PARAM_NAME);
        if (null == unix) {
            return;
        }
        String datePattern = StringUtils.defaultIfBlank(getStrParam(params, PATTERN_PARAM_NAME), DateUtils.DATE_TIME_PATTERN);
        out.write(DateUtils.format(DateUtils.getSecondTimeStampDate(Long.valueOf(unix)), datePattern));
    }

}
