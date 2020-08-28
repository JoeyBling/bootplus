package io.github.frame.prj.freemaker;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 简易FreeMarker指令标签接口实现
 *
 * @author Created by 思伟 on 2020/8/28
 */
public abstract class SimpleTemplateDirectiveModel implements
        MyTemplateModel<TemplateDirectiveModel>, TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        if (requireBody() && body == null) {
            throw new TemplateModelException("自定义标签必须有内容，即自定义开始标签与结束标签之间必须有内容");
        }
        verifyParameters(params);
        render(env.getOut(), params);
        renderBody(env, body);
    }

    /**
     * 自定义标签必须有内容，即自定义 开始标签与结束标签之间必须有 内容
     *
     * @return boolean
     */
    protected boolean requireBody() {
        return true;
    }

    /**
     * 验证参数
     *
     * @param params Map
     * @throws TemplateModelException
     */
    protected void verifyParameters(Map params) throws TemplateModelException {
    }

    /**
     * 子类实现渲染
     *
     * @param out    Writer
     * @param params Map
     * @throws TemplateModelException
     * @throws IOException
     */
    protected abstract void render(Writer out, Map params) throws TemplateModelException, IOException;

    /**
     * 渲染body
     *
     * @param env
     * @param body
     * @throws IOException
     * @throws TemplateException
     */
    protected void renderBody(Environment env, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (body != null) {
            body.render(env.getOut());
        }
    }

    /**
     * 获取字符串类型的值
     *
     * @param params 入参
     * @param name   参数名
     * @return String
     */
    protected String getStrParam(Map params, String name) {
        Object value = params.get(name);

        return getStrValue(value);
    }

}