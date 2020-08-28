package io.github.frame.prj.freemaker;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import io.github.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 简易FreeMarker方法标签接口实现
 *
 * @author Created by 思伟 on 2020/8/28
 */
public abstract class SimpleTemplateMethodModelEx implements
        MyTemplateModel<TemplateMethodModelEx>, TemplateMethodModelEx {

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        verifyParameters(arguments);
        // 为空返回空白字符串,防止报错
        return Optional.ofNullable(getValue(arguments)).orElse(new SimpleScalar(StringUtils.EMPTY));
    }

    /**
     * 验证参数
     *
     * @param args List
     * @throws TemplateModelException
     */
    protected void verifyParameters(List args) throws TemplateModelException {
    }

    /**
     * 获取值
     *
     * @param args List
     * @return Object
     * @throws TemplateModelException
     */
    protected abstract Object getValue(List args) throws TemplateModelException;

}
