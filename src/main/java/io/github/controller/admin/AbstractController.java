package io.github.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.config.aop.service.BaseAopContext;
import io.github.entity.SysUserEntity;
import io.github.util.config.Constant;
import io.github.util.spring.EhcacheUtil;
import io.github.util.spring.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller公共组件
 * 要想继承实现@RequestMapping和@ResponseBody 父类的访问修饰符必须是public，不然获取到的方法和实际的方法不一致
 *
 * @author Joey
 * @Email 2434387555@qq.com
 * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#detectHandlerMethods(Object)
 * @see org.springframework.core.MethodIntrospector#selectMethods(Class, MethodIntrospector.MetadataLookup)
 * @see org.springframework.util.ClassUtils#getMostSpecificMethod(Method, Class)
 */
public abstract class AbstractController<S> extends BaseAopContext<S> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 常量帮助类
     */
    @Resource
    protected Constant constant;

    @Resource
    protected EhcacheUtil ehcacheUtil;

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
     * 事实证明@RequestMapping...等注解不可以被继承
     * 注解需要添加@Inherited 才可以被继承
     */
//    @RequestMapping("/Inherited")
    @ResponseBody
    @Deprecated
    public boolean testInherited(Model model) {
        return true;
//        return "test-Inherited测试";
    }

    //    @RequestMapping("/testString")
    @ResponseBody
    @Deprecated
    public String testString(Model model) {
        return "测试String乱码问题";
    }

}
