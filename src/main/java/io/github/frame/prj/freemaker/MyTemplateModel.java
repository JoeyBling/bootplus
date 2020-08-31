package io.github.frame.prj.freemaker;

import freemarker.template.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义FreeMarker标签接口
 *
 * @author Created by 思伟 on 2020/8/28
 */
public interface MyTemplateModel<T extends TemplateModel> extends TemplateModel {

    /**
     * 获取访问标签的名称
     *
     * @return String
     * @see Configuration#setSharedVariable
     */
    default String getTagName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 获取字符串类型的值(如果不是字符串类型返回null)
     * <code>null</code>.
     *
     * @param value Object
     * @return String
     */
    default String getStrValue(Object value) {
        if (value instanceof TemplateScalarModel) {
            try {
                return ((TemplateScalarModel) value).getAsString();
            } catch (TemplateModelException e) {

            }
        }
        return null;
    }

    /**
     * 获取List类型的值(如果不是返回null)
     * <code>null</code>.
     *
     * @param value Object
     * @return List
     */
    default List<TemplateModel> getListValue(Object value) {
        if (value instanceof TemplateSequenceModel) {
            try {
                final TemplateSequenceModel sequenceModel = (TemplateSequenceModel) value;
                final int size = sequenceModel.size();
                List<TemplateModel> list = new ArrayList<>(size);
                for (int i = 0; size > 0 && i < size; i++) {
                    list.add(sequenceModel.get(i));
                }
                return list;
            } catch (TemplateModelException e) {

            }
        }
        return null;
    }

}