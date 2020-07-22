package io.github.test;

import com.caucho.hessian.client.HessianProxyFactory;
import org.junit.Test;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Hessian工具类
 *
 * @author Created by 思伟 on 2020/6/6
 */
public class HessianUtil {

    public static String url = "http://test-wechat-repeater.hztywl.cn";

    static {
//        url = "http://localhost:8000";
        url = "http://dev-saas.diandianys.com/api";
    }

    private static Map<Class, Object> clientMap = new HashMap<Class, Object>();

    public static <T> T getClient(Class<T> clazz, String apiName) {
        T obj = (T) clientMap.get(clazz);
        if (obj != null) {
            return obj;
        }
        obj = initClient(clazz, apiName);
        clientMap.put(clazz, obj);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T> T initClient(Class<T> clazz, String apiName) {
        HessianProxyFactoryBean bean = new HessianProxyFactoryBean();
        HessianProxyFactory factory = new HessianProxyFactory();
        bean.setProxyFactory(factory);
        bean.setServiceInterface(clazz);
        bean.setServiceUrl(url + apiName);
        bean.setOverloadEnabled(true);

        bean.afterPropertiesSet();
        return (T) bean.getObject();
    }

    @Test
    public void test() {
//        IWechatAppService wechatAppService = getClient(IWechatAppService.class, "/IWechatAppService");
//        WechatApp appid = wechatAppService.getByAppid("wxa49f90b4ff678ef2");
//        System.out.println(JSON.toJSONString(appid));
    }

}