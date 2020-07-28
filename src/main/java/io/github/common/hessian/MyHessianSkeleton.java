package io.github.common.hessian;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.server.HessianSkeleton;
import io.github.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;

/**
 * 此类主要是为了获取Hessian请求参数
 * TODO 暂时拿不到，建议存head吧
 * Proxy class for Hessian services.
 *
 * @author Created by 思伟 on 2020/7/27
 * @since 4.x
 */
@Deprecated
@Slf4j
public class MyHessianSkeleton extends HessianSkeleton {

    public MyHessianSkeleton(Object service, Class<?> apiClass) {
        super(service, apiClass);
    }

    public MyHessianSkeleton(Class<?> apiClass) {
        super(apiClass);
    }

    @Override
    public void invoke(Object service, AbstractHessianInput in, AbstractHessianOutput out) throws Exception {

        // 参考父类进行重写获取请求参数
        /*String methodName = in.readMethod();
        int argLength = in.readMethodArgLength();

        Method method = getMethod(methodName + "__" + argLength);

        if (method == null) {
            method = getMethod(methodName);
        }
        if (method != null) {
            Class<?>[] args = method.getParameterTypes();
            Object[] values = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                values[i] = in.readObject(args[i]);
            }
            if (ArrayUtils.isNotEmpty(args)) {
                for (Object value : values) {
                    log.debug("请求参数：{}", StringUtils.toString(value));
                }
            }
        }*/
        super.invoke(service, in, out);
    }

}
