package io.github.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 静态代码块，静态方法，构造方法的执行顺序
 *
 * @author Created by 思伟 on 2020/10/13
 */
public class StaticTest {

    static int age = 1;
    private static Map<String, HashMap<?, ?>> cacheMap = new HashMap<>();

    static {
        System.out.println("这是静态代码块");
    }

    int age1 = 2;

    {
        System.out.println("这是普通代码块" + age1);
    }

    public StaticTest() {
        System.out.println("这是构造方法");
    }

    public static void show() {
        System.out.println("这是静态方法");
        System.out.println(cacheMap.size());
    }

    public static void main(String[] args) {
        //System.out.println(age);
        StaticTest.show();
        //StaticTest t = new StaticTest();
        //t.fun();
        /*System.out.println(t.age1);*/

        //String a = Boolean.toString(true);
        // System.out.println(a);
    }

    public void fun() {
        System.out.println("这是普通方法");
    }

}
