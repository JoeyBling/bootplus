package io.github.test;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.common.enums.IEnumHelperFactory;
import io.github.config.filter.MyCorsFilter;
import io.github.entity.enums.SysMenuTypeEnum;
import io.github.util.file.FileTypeEnum;
import io.github.util.http.RestTemplateUtil;
import io.github.util.log.LogUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by 思伟 on 2020/1/9
 */
public class _Main {

    public static Map<String, String> bTOS(Map<?, ?> map) {
        return (HashMap<String, String>) map;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException {
        System.out.println(Collections.<String>emptyList());
        // 默认的临时目录
        System.out.println(System.getProperty("java.io.tmpdir"));
        // 用户的家目录
        System.out.println(System.getProperty("user.home"));
        // 用户当前的工作目录
        System.out.println(System.getProperty("user.dir"));
        Map<String, Boolean> testMap1 = new HashMap<String, Boolean>(2);
        testMap1.put("1", true);
        testMap1.put("2", false);

        Map<String, String> testMap11 = bTOS(testMap1);

        for (Iterator<String> nameIterator = testMap11.keySet().iterator(); nameIterator.hasNext(); ) {
            String name = nameIterator.next();
            System.out.println(name);
            //  java.lang.Boolean cannot be cast to java.lang.String
//            System.out.println(testMap11.get(name));
        }

        ImmutableMap<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("start_time", System.currentTimeMillis())
                .put("enable_auto_cloud_recording", true).build();
//        System.out.println(RestTemplateUtil.postForObject("https://www.baidu.com/", map, MediaType.APPLICATION_FORM_URLENCODED));
        System.out.println(Thread.currentThread().getName());
        RestTemplateUtil.postForObject("https://www.gerensuodeshui.cn/", null, String.class);
        System.out.println(Thread.currentThread().getName());

        RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map);
        RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map);
        RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map);
        System.out.println(Thread.currentThread().getName());

//        System.out.println(RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map));


        System.out.println("test2：" + test2());
        LogUtil.MyLogger logger = LogUtil.getInstance().getInterceptorStatementLogger();
        logger = LogUtil.getInstance().getInterceptorStatementLogger();
//        logger.info(Enum.valueOf(SysMenuTypeEnum.class, "0").getValue());
        IEnumHelperFactory.IEnumHelper sysMenuTypeHelper = IEnumHelperFactory.getInstance().getByClass(
                SysMenuTypeEnum.class);
        logger.info(IEnumHelperFactory.getInstance().getByClass(
                SysMenuTypeEnum.class).getByKey("0").getValue());
        logger.info(IEnumHelperFactory.getInstance().getByClass(
                FileTypeEnum.class).getByKey("IMAGE").getValue());
        String[] keyArray = sysMenuTypeHelper.getKeyArray();
        List list = sysMenuTypeHelper.getList();
        Logger finalLogger = logger.getLogger();
        list.forEach(o -> {
            finalLogger.info(o.toString());
        });
        for (String key : keyArray) {
            logger.info(key);
        }

        // 要想继承实现@RequestMapping和@ResponseBody 父类的访问修饰符必须是public，不然获取到的方法和实际的方法不一致
        Class<?> sysClass = Class.forName("io.github.controller.admin.SysLoginController");
        for (Method method : sysClass.getMethods()) {
            System.out.println(method.getName());
        }
        Method testString = sysClass.getMethod("testString", Model.class);
        JSONField annotation = TypeUtils.getAnnotation(testString, JSONField.class);
        annotation = AnnotationUtils.getAnnotation(testString, JSONField.class);
        annotation = AnnotationUtils.findAnnotation(testString, JSONField.class);
        annotation = AnnotatedElementUtils.findMergedAnnotation(testString, JSONField.class);
        System.out.println(testString);
        // 不同的平台生成相应平台的换行符
        /** java中的转义符:\r\n
         windows下的文本文件换行符:\r\n
         linux/unix下的文本文件换行符:\r
         Mac下的文本文件换行符:\n */
        // 获取操作系统对应的换行符
        System.out.println(File.pathSeparator);
        System.out.println(System.getProperty("line.separator", "\n"));
        System.out.println(MyCorsFilter.class.getSimpleName());
        System.out.println(MyCorsFilter.class.getName());
        System.out.println(MyCorsFilter.class.getTypeName());
        String[] array = ArrayUtils.toArray(
                HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name());
        System.out.println(StringUtils.join(array, ", "));
        System.out.println(ArrayUtils.toString(array
                , StringUtils.EMPTY));
        System.out.println(new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringBuilder.getDefaultStyle()).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.MULTI_LINE_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.SHORT_PREFIX_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.NO_CLASS_NAME_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.NO_FIELD_NAMES_STYLE).append(array).toString());

        // Spring原生线程池【写简书】
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        /* ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 
        ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。 
        ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，执行后面的任务
        ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务 */
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        singleThreadPool.shutdown();


        // 压缩文件夹进行Base64编码再写入文件存储
//        Map<String, Key> rsa = CryptoUtil.initKeyPair("RSA", 1024, new BouncyCastleProvider());
//        System.out.println(Base64.encodeBase64String(rsa.get(CryptoUtil.PRIVATE_KEY).getEncoded()));
//        System.out.println(Base64.encodeBase64String(rsa.get(CryptoUtil.PUBLIC_KEY).getEncoded()));
//
//        File file = new File("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\server-net\\server-net.zip");
//        FileInputStream inputFile = new FileInputStream(file);
//        byte[] buffer = new byte[(int) file.length()];
//        inputFile.read(buffer);
//        inputFile.close();
//        String base64Code = Base64.encodeBase64String(buffer);
//        FileUtils.writeStringToFile(new File("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\server-net\\smarthos-admin\\src\\main\\webapp\\tes.txt"), base64Code, "UTF-8");

//        byte[] bytes = Base64.decodeBase64(FileUtils.readFileToString(new File("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\code.txt"), Charset.forName("UTF-8")));
//        FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\code.rar");
//        out.write(bytes);
//        out.close();

    }


    public static int test2() {
        int i = 1;
        try {
            System.out.println("try语句块中");
            return 1;
        } finally {
            System.out.println("finally语句块中");
            return 2;
        }

    }

}
