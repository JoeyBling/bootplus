package io.github.util.spring;

import io.github.util.RegexUtil;
import io.github.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring字符串替换回调接口实现（支持SpEL表达式）
 * TODO 目前只支持单个配置文件解析
 *
 * @author Created by 思伟 on 2020/7/29
 */
@Slf4j
public class SpringReplaceCallBack implements RegexUtil.ReplaceCallBack {

    /**
     * 是否启用日志
     */
    private boolean logEnabled = true;

    /**
     * 数据集合
     */
    private Map<?, ?> dataMap;

    /**
     * 唯一构造函数
     *
     * @param dataMap Map
     */
    public SpringReplaceCallBack(Map<?, ?> dataMap) {
        this(true, dataMap);
    }

    public SpringReplaceCallBack(boolean logEnabled, Map<?, ?> dataMap) {
        Assert.notNull(dataMap, "dataMap must not be null");
        this.logEnabled = logEnabled;
        this.dataMap = dataMap;
    }

    /**
     * Spring SpEL表达式匹配正则
     */
    public static final String SP_EL_REGEX = "\\$\\{(\\w+(\\.\\w+)*)(\\:(\\w+))?\\}";

    /**
     * 替换正则表达式对象
     * 测试发现-如果是普通main方法调用，获取的对象为null，暂时改为类属性(有待改进)
     */
    public final Pattern PATTERN = Pattern.compile(SP_EL_REGEX);

    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    @Override
    public String replace(String text, int index, Matcher matcher) {
        if (logEnabled && log.isDebugEnabled()) {
            log.debug("开始解析SpEL表达式:{}", text);
        }
        // 从第一个分组()开始取
        final String matcherGroup = matcher.group(1);
        String defaultStr = null;
        try {
            // 允许默认值(暂时支持英文) e.g. ${application.name:zhousiwei}-task-
            defaultStr = StringUtils.defaultString(matcher.group(4), matcherGroup);
        } catch (Exception e) {
        }
        // Map嵌套解析
        String value = getMapValue(this.dataMap, matcherGroup);
        // 支持多层嵌套解析
        value = RegexUtil.replaceAll(value, this);
        return StringUtils.defaultString(value, defaultStr);
    }

    /**
     * 从Map中读取出具体的属性值(支持Map里面嵌多个Map)
     *
     * @param map Map
     * @param key 参数key
     * @return String
     */
    protected String getMapValue(Map<?, ?> map, String key) {
        if (map == null || map.isEmpty()) {
            return key;
        }
        // 如果存在key直接返回
        if (null != map.get(key)) {
            return StringUtils.toString(map.get(key));
        }
        String[] keySplit = key.split("\\.");
        Object o = map.get(keySplit[0]);
        if (o == null) {
            return null;
        }
        if (keySplit.length > 1) {
            // 递归解析
            return getMapValue((HashMap) o, key.substring(keySplit[0].length() + 1));
        }
        return StringUtils.toString(map.get(key));
    }

}
