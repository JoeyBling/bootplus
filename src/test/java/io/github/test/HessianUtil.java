package io.github.test;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;
import io.github.common.hessian.HessianHeaderContext;
import io.github.entity.SysUserLoginLogEntity;
import io.github.frame.prj.enums.ShortLinkForwardModeEnum;
import io.github.frame.prj.model.ShortLinkVO;
import io.github.service.SysUserLoginLogService;
import io.github.service.hessian.IExtShortLinkService;
import io.github.util.PageUtils;
import org.junit.Test;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
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
//        url = "http://dev-saas.diandianys.com/api";
        url = "http://localhost";
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
        HessianProxyFactory factory = new MyHessianProxyFactory();
        bean.setProxyFactory(factory);
        bean.setServiceInterface(clazz);
        bean.setServiceUrl(url + apiName);
        bean.setOverloadEnabled(true);
//        bean.setUsername("joey");
//        bean.setPassword("joey");
        bean.afterPropertiesSet();
        return (T) bean.getObject();
    }

    @Test
    public void test() {
        HessianHeaderContext.getInstance().addHeader("test", "Joey")
                .addHeader("testName", "思伟_");
        SysUserLoginLogService sysUserLoginLogService = getClient(SysUserLoginLogService.class, "/ExtSysUserLoginLogService");
        Page<SysUserLoginLogEntity> page = sysUserLoginLogService.getPage(1, 20, 1L, null, null, null);

        HessianHeaderContext.getInstance(true).addHeader("test", "Joey2")
                .addHeader("testName", "思伟2");
        page = sysUserLoginLogService.getPage(1, 20, 1L, null, null, null);
        final PageUtils<SysUserLoginLogEntity> pageUtils = PageUtils.buildPageUtil(page);
        pageUtils.getList().forEach(entity -> {
            System.out.println(entity);
        });

        final IExtShortLinkService iExtShortLinkService = getClient(IExtShortLinkService.class, "/IExtShortLinkService");
        final ShortLinkVO shortLinkVO = iExtShortLinkService.generateShortLink(ShortLinkForwardModeEnum.REDIRECT, "https://zhousiwei.gitee.io/ibooks/");
        System.out.println(shortLinkVO);

    }

    /**
     * 拓展HessianProxy，在客户端发送请求之前，添加自定义请求头信息
     *
     * @author Created by 思伟 on 2020/7/27
     * @see HessianHeaderContext
     */
    public static class MyHessianProxy extends HessianProxy {

        protected MyHessianProxy(URL url, HessianProxyFactory factory) {
            super(url, factory);
        }

        public MyHessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
            super(url, factory, type);
        }

        @Override
        protected void addRequestHeaders(HessianConnection conn) {
            super.addRequestHeaders(conn);
            // add Hessian Header
            try {
                Map<String, String> headerMap = HessianHeaderContext.getInstance().getHeaders();
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    conn.addHeader(entry.getKey(), entry.getValue());
                }
            } finally {
                HessianHeaderContext.clear();
            }
        }
    }

    /**
     * 拓展HessianProxyFactory
     *
     * @author Created by 思伟 on 2020/7/27
     */
    public static class MyHessianProxyFactory extends HessianProxyFactory {

        @Override
        public Object create(Class<?> api, URL url, ClassLoader loader) {
            if (api == null)
                throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
            InvocationHandler handler = null;

            handler = new MyHessianProxy(url, this, api);

            return Proxy.newProxyInstance(loader,
                    new Class[]{api, HessianRemoteObject.class}, handler);
        }
    }

}