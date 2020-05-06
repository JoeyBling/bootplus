package io.github.test;

import java.util.Optional;

/**
 * 使用 JDK8 的 Optional 类来防止 NPE 问题
 * 【java8新特性】Optional详解
 *
 * @author Created by 思伟 on 2020/5/6
 * @since 1.8+
 */
public class OptionalTest {

    public static void main(String[] args) {
        // 1、创建一个包装对象值为空的Optional对象
        Optional<String> optStr = Optional.empty();
        // 2、创建包装对象值非空的Optional对象
        Optional<String> optStr1 = Optional.of("optional");
        // 3、创建包装对象值允许为空的Optional对象
        Optional<String> optStr2 = Optional.ofNullable(null);
    }

}
