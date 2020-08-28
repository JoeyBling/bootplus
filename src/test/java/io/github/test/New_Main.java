package io.github.test;

import io.github.controller.admin.SysTaskController;
import io.github.util.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Main Test
 *
 * @author Created by 思伟 on 2020/1/9
 */
public class New_Main {

    public static void main(String[] args) throws Exception {

        char c = 'D';
        System.out.println(c + 1);

        Calendar lastMonth = Calendar.getInstance();
        lastMonth.setTime(DateUtils.parseDateStr("2020-06-30"));
        lastMonth.add(Calendar.MONTH, -1);
        System.out.println(DateUtils.format(lastMonth.getTime()));

        Calendar now = Calendar.getInstance();
        now.setTime(lastMonth.getTime());
        now.add(Calendar.MONTH, 1);
        System.out.println(DateUtils.format(now.getTime()));

        // 获取父类注解
        Class<?> testClass = SysTaskController.class;
        for (Method method : testClass.getMethods()) {
            System.out.println(method.getName());
            RequiresPermissions annotation = null;
            annotation = AnnotationUtils.getAnnotation(method, RequiresPermissions.class);
            // false
            //System.out.println(annotation != null);
            annotation = AnnotationUtils.findAnnotation(method, RequiresPermissions.class);
            // True
            System.out.println(annotation != null);
            annotation = AnnotatedElementUtils.findMergedAnnotation(method, RequiresPermissions.class);
            // True
            System.out.println(annotation != null);
        }
    }


}
