package io.github.common.enums;

/**
 * IEnum的简单抽象实现
 *
 * @author Created by 思伟 on 2020/8/31
 * @deprecated enum只可以实现接口，不能继承
 */
@Deprecated
public abstract class BaseEnum implements IEnum {

    @Override
    public String toString() {
        return this.getKey();
    }

}
