package io.github.common.enums;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 枚举帮助工厂类
 *
 * @author Created by 思伟 on 2020/4/1
 */
public class IEnumHelperFactory {

    /**
     * 枚举帮助内部类缓存Map
     */
    private Map<Class<? extends Object>, IEnumHelper<? extends Object>>
            I_ENUM_MAP = Maps.newConcurrentMap();

    public <E extends Enum<?> & IEnum> IEnumHelper<E> getByClass(Class<E> enumClass) {
        // Map<Class<E>, IEnumHelper<E>> I_ENUM_MAP = Maps.newConcurrentMap();
        // 获取枚举帮助内部类
        IEnumHelper<E> enumHelper = (IEnumHelper<E>) I_ENUM_MAP.get(enumClass);
        if (null == enumHelper) {
            // 缓存不存在，进行实例化
            enumHelper = new IEnumHelper<E>(enumClass);
            I_ENUM_MAP.put(enumClass, enumHelper);
        }
        return enumHelper;
    }

    /**
     * 外部不提供实例化方法
     */
    private IEnumHelperFactory() {
        super();
    }

    /**
     * 枚举帮助工厂类
     */
    private static IEnumHelperFactory iEnumHelperFactory;

    /**
     * 获取唯一实例对象
     */
    public static IEnumHelperFactory getInstance() {
        if (null == iEnumHelperFactory) {
            // 支持对任意对象作为对象监视器来实现同步功能，非this对象
            synchronized (IEnumHelperFactory.class) {
                if (null == iEnumHelperFactory) {
                    iEnumHelperFactory = new IEnumHelperFactory();
                }
            }
        }
        return iEnumHelperFactory;
    }

    /**
     * 枚举帮助内部类
     *
     * @param <E>
     */
    public static class IEnumHelper<E extends Enum<?> & IEnum> {

        private Class<E> enumClass;

        public IEnumHelper(Class<E> enumClass) {
            if (enumClass == null) {
                throw new IllegalArgumentException("Type argument cannot be null");
            }
            this.enumClass = enumClass;
        }

        public E getByKey(String key) {
            E[] es = enumClass.getEnumConstants();
            for (int i = 0; i < es.length; i++) {
                if (es[i].getKey().equals(key)) {
                    return es[i];
                }
            }
            return null;
        }

        /**
         * 获取所有Key值
         */
        public String[] getKeyArray() {
            E[] es = enumClass.getEnumConstants();
            if (null == es) {
                return null;
            }
            String[] keys = new String[es.length];
            for (int i = 0; i < es.length; i++) {
                keys[i] = es[i].getKey();
            }
            return keys;
        }

        /**
         * 获取List
         */
        public List<E> getList() {
            E[] es = enumClass.getEnumConstants();
            List<E> enumList = new ArrayList<E>();
            for (int i = 0; null != es && i < es.length; i++) {
                enumList.add(es[i]);
            }
            return enumList;
        }

    }

}
