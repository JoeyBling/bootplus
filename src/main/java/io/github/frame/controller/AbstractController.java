package io.github.frame.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.config.ApplicationProperties;
import io.github.config.aop.service.BaseAopContext;
import io.github.entity.SysUserEntity;
import io.github.frame.constant.SystemConst;
import io.github.util.spring.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodIntrospector;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller公共组件
 * 要想继承实现@RequestMapping和@ResponseBody 父类的访问修饰符必须是public，不然获取到的方法和实际的方法不一致
 *
 * @author Created by 思伟 on 2020/6/6
 * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#detectHandlerMethods(Object)
 * @see org.springframework.core.MethodIntrospector#selectMethods(Class, MethodIntrospector.MetadataLookup)
 * @see org.springframework.util.ClassUtils#getMostSpecificMethod(Method, Class)
 */
public abstract class AbstractController<S> extends BaseAopContext<S> {

    /**
     * 默认编码
     */
    protected final Charset DEFAULT_CHARSET = SystemConst.DEFAULT_CHARSET;

    /**
     * 安全随机数实现
     */
    protected final SecureRandom secureRandom = new SecureRandom();

    /**
     * 版本号
     */
    protected final long T_VERSION = System.currentTimeMillis();

    /**
     * 重定向标识
     */
    protected String redirect = "redirect:";

    /**
     * 转发请求标识
     */
    protected String forward = "forward:";

    /**
     * 程序自定义配置
     */
    @Resource
    protected ApplicationProperties applicationProperties;

    /**
     * 重定向
     */
    public String getRedirect(String path) {
        return redirect + path;
    }

    /**
     * 转发
     */
    public String getForward(String path) {
        return forward + path;
    }

    /**
     * 获取当前登录管理员
     *
     * @return 管理员
     */
    protected SysUserEntity getAdmin() {
        return ShiroUtils.getAdminEntity();
    }

    /**
     * 获取当前登录管理员ID
     *
     * @return 管理员ID
     */
    protected Long getAdminId() {
        return ShiroUtils.getUserId();
    }

    /**
     * 解析成一个数组(批量操作用)
     *
     * @param ja JSONArray
     * @return Long[]
     */
    protected Long[] toArrays(JSONArray ja) {
        Long[] objs = new Long[ja.size()];
        for (int i = 0; i < ja.size(); i++) {
            objs[i] = Long.valueOf(ja.get(i).toString());
        }
        return objs;
    }

    /**
     * 根据JSON字符串返回对应的Value
     *
     * @param search   要解析Json的字符串
     * @param keyNames 查询的Names
     * @return Map<String, T>
     */
    @SuppressWarnings("unchecked")
    protected <T> Map<String, T> parseObject(String search, String... keyNames) {
        JSONObject parseObject = JSONArray.parseObject(search);
        if (null != parseObject && null != keyNames) {
            Map<String, T> map = new HashMap<String, T>(5);
            for (String key : keyNames) {
                Object value = parseObject.get(key);
                map.put(key, (T) value);
            }
            return map;
        }
        return null;
    }

    /**
     * 排序规则是否为ASC正序
     */
    protected boolean isOrderByAsc(String order) {
        if (StringUtils.isNoneBlank(order)) {
            String asc = "asc";
            if (asc.equalsIgnoreCase(order)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取当前页
     */
    protected int getOffset(Integer offset) {
        if (null == offset || offset.intValue() < 0) {
            return 0;
        }
        return offset;
    }

    /**
     * 获取页数
     */
    protected int getLimit(Integer limit) {
        if (null == limit || limit.intValue() < 0) {
            return 20;
        }
        return limit;
    }

    //    @RequestMapping("/testString")
    @ResponseBody
    @Deprecated
    public String testString(Model model) {
        return "测试String乱码问题";
    }

}
