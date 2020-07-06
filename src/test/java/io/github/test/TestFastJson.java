package io.github.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.Data;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 父类的方法有@JSONField，子类重写发现注解被丢失
 *
 * @author Created by 思伟 on 2020/4/20
 */
public class TestFastJson {

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(new B()));

        // 获取父类注解
        Class<?> bClass = B.class;
        for (Method method : bClass.getDeclaredMethods()) {
//        for (Method method : bClass.getMethods()) {
            System.out.println(method.getName());
            // FastJson内置获取注解无法获取
            Class<? extends Annotation> annotationClass = JSONField.class;
            Annotation annotation = TypeUtils.getAnnotation(method, annotationClass);
            // false
            System.out.println(annotation != null);
            annotation = AnnotationUtils.getAnnotation(method, annotationClass);
            // false
            System.out.println(annotation != null);
            annotation = AnnotationUtils.findAnnotation(method, annotationClass);
            // True
            System.out.println(annotation != null);
            annotation = AnnotatedElementUtils.findMergedAnnotation(method, annotationClass);
            // True
            System.out.println(annotation != null);
        }
    }

    interface Entity<T extends CharSequence> extends Serializable {
        T getModule(T t);
    }

    static abstract class A<T extends CharSequence> implements Entity<T> {

        @Override
        @JSONField(serialize = false)
        public T getModule(T t) {
            return t;
        }

        @JSONField(serialize = false)
        public String getTest() {
            return "";
        }

    }

    static class B extends A<String> {

        @Override
        public String getModule(String str) {
            return this.getClass().getSimpleName();
        }

        @Override
        public String getTest() {
            return "123";
        }
    }


}
