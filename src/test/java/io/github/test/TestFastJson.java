package io.github.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.Serializable;
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
            // FastJson内置获取注解无法获取
            JSONField annotation = TypeUtils.getAnnotation(method, JSONField.class);
            // false
            System.out.println(annotation != null);
            annotation = AnnotationUtils.getAnnotation(method, JSONField.class);
            // false
            System.out.println(annotation != null);
            annotation = AnnotationUtils.findAnnotation(method, JSONField.class);
            // True
            System.out.println(annotation != null);
            annotation = AnnotatedElementUtils.findMergedAnnotation(method, JSONField.class);
            // True
            System.out.println(annotation != null);
        }

    }

    interface Entity extends Serializable {
        String getModule();
    }

    static class A implements Entity {

        @Override
        @JSONField(serialize = false)
        public String getModule() {
            return "AAA";
        }
    }

    static class B extends A {

        @Override
        public String getModule() {
            return this.getClass().getSimpleName();
        }

    }


}
