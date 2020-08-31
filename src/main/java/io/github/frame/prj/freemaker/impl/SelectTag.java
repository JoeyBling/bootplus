package io.github.frame.prj.freemaker.impl;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import io.github.frame.prj.freemaker.SimpleTemplateDirectiveModel;
import io.github.util.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * select下拉框生成
 *
 * @author Created by 思伟 on 2020/8/28
 */
@Component
public class SelectTag extends SimpleTemplateDirectiveModel {

    @Override
    public String getTagName() {
        return "select";
    }

    /**
     * 默认css样式
     * chosen-select 带搜索框
     * form-control m-b 不带搜索框
     */
    private final String DEFAULT_CSS = "form-control";

    @Override
    protected boolean requireBody() {
        return false;
    }

    @Override
    protected void render(Writer out, Map params) throws TemplateModelException, IOException {
        // 暂时支持List，后续考虑支持Map、Collection...
        final List<TemplateModel> items = getListValue(params.get("items"));
        if (ObjectUtils.isEmpty(items)) {
            return;
        }
        StringBuffer sb = new StringBuffer();


        // init select
        sb.append("<select class=\"");
        sb.append(StringUtils.defaultString(getStrParam(params, "class"), DEFAULT_CSS));
        sb.append("\" name=\"");
        sb.append(StringUtils.defaultString(getStrParam(params, "name")));
        sb.append("\">");

        // 默认选择框
        String defaultLabel = getStrParam(params, "defaultLabel");
        if (StringUtils.isNotEmpty(defaultLabel)) {
            sb.append("<option value=\"");
            sb.append(StringUtils.defaultString(getStrParam(params, "defaultValue")));
            sb.append("\">");
            sb.append(defaultLabel);
            sb.append("</option>");
        }

        // init options
        final String selectVal = getStrParam(params, "val");
        for (TemplateModel item : items) {
            final Object object = ((WrapperTemplateModel) item).getWrappedObject();
//            if (List.class.isAssignableFrom(items.getClass())) {
//            }
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
            String label = StringUtils.toString(wrapper.getPropertyValue(getStrParam(params, "itemLabel")));
            String value = StringUtils.toString(wrapper.getPropertyValue(getStrParam(params, "itemValue")));
            sb.append("<option value=\"");
            sb.append(StringUtils.defaultString(value));
            sb.append(StringUtils.equals(selectVal, value) ? "\" selected>" : "\">");
            sb.append(label);
            sb.append("</option>");
        }

        sb.append("</select>");
        out.write(sb.toString());
    }

}
