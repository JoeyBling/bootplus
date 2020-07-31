package io.github.frame.log.logback.property;

import ch.qos.logback.core.PropertyDefinerBase;
import io.github.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * 抽象父类Property Definer
 *
 * @author Created by 思伟 on 2020/5/20
 */
public abstract class BasePropertyDefiner extends PropertyDefinerBase {

    /**
     * FIXME
     * A number (N) of logging calls during the initialization
     * phase have been intercepted and are now being replayed.
     * These are subject to the filtering rules of the underlying logging system.
     */
    @Deprecated
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 简单异常信息
     */
    protected String getSimpleErrMsg() {
        return MessageFormat.format("Execute Property Definer [{0}] failed"
                , ClassUtil.getClass(this));
    }

    @Override
    public String getPropertyValue() {
        try {
            return getValue();
        } catch (Throwable e) {
            addError(getSimpleErrMsg());
        }
        return null;
    }

    /**
     * 获取属性值
     *
     * @return 自定义属性值
     */
    protected abstract String getValue();

    /**
     * 判断是否为空字符串
     *
     * @param cs CharSequence
     * @return boolean
     */
    protected boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

}
