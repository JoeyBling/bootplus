package io.github.test;

import io.github.util.RegexUtil;
import io.github.util.spring.SpringReplaceCallBack;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test Main
 *
 * @author Created by 思伟 on 2020/7/29
 */
public class Test {

    public static void main(String[] args) {
        String str = "${no.property:English_only}-${application.basedir}/../${application.title}-${application.log.dirName}";
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, String> applicationMap = new HashMap<>();

        // 第一种情况
//        dataMap.put("application.basedir", "/home/siwei");
//        dataMap.put("application.title", "${application.name}");
//        dataMap.put("application.name", "bootplus");
//        dataMap.put("application.log.dirName", "logs");

        // 第二种情况
        dataMap.put("application", applicationMap);
        applicationMap.put("basedir", "/home/siwei");
        applicationMap.put("title", "${application.name}");
        applicationMap.put("name", "bootplus");
        applicationMap.put("log.dirName", "logs");
        System.out.println(RegexUtil.replaceAll(str, new SpringReplaceCallBack(dataMap)));

        System.out.println(RegexUtil.replaceAll("http://bootplus.diandianys.com/admin", new RegexUtil.ReplaceCallBack() {

            /**
             * 替换正则表达式对象
             */
            public final Pattern PATTERN = Pattern.compile("a");

            @Override
            public Pattern getPattern() {
                return PATTERN;
            }

            @Override
            public String replace(String text, int index, Matcher matcher) {
//                return text;
                return "A" + index;
            }
        }));
    }


}


