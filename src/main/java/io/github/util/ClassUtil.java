package io.github.util;

import org.springframework.util.Assert;

/**
 * Class工具类
 *
 * @author Created by 思伟 on 2020/1/10
 */
public class ClassUtil {

    /**
     * 获取包路径
     */
    public static String getPackageName(Object obj) throws Exception {
        Assert.notNull(obj, "obj must not be null");
        return getPackageName(AopTargetUtils.getTarget(obj).getClass());
    }

    public static String getPackageName(Class obj) {
        Assert.notNull(obj, "obj must not be null");
        return obj.getPackage().getName();
    }

    /**
     * 获取类名
     */
    public static String getClassName(Object obj) throws Exception {
        Assert.notNull(obj, "obj must not be null");
        return getClassName(AopTargetUtils.getTarget(obj).getClass());
    }

    public static String getClassName(Class obj) {
        Assert.notNull(obj, "obj must not be null");
        return obj.getSimpleName();
    }

    /**
     * 获取类路劲
     */
    public static String getClass(Object obj) throws Exception {
        Assert.notNull(obj, "obj must not be null");
        return getClass(AopTargetUtils.getTarget(obj).getClass());
    }

    public static String getClass(Class obj) {
        Assert.notNull(obj, "obj must not be null");
        return obj.getName();
    }


    public static void main(String[] args) {
        System.out.println(ClassUtil.getClass(ClassUtil.class));
        System.out.println(ClassUtil.getClass(null));
    }
}
