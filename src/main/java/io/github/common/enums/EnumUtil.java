package io.github.common.enums;

/**
 * 枚举工具类
 *
 * @author Created by 思伟 on 2020/4/1
 */
public class EnumUtil {

    /**
     * 获取枚举值
     *
     * @param enumClass E
     * @param key       key
     * @param <E>       IEnum
     * @return IEnum
     */
    public static <E extends Enum<?> & IEnum> E keyOf(Class<E> enumClass, String key) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (null == enumConstants) {
            throw new IllegalArgumentException("Enum data cannot be null");
        }
        for (E e : enumConstants) {
            if (e.getKey().equals(key)) {
                return e;
            }
        }
        return null;
    }

}
