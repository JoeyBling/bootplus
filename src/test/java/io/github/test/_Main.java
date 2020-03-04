package io.github.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.config.filter.MyCorsFilter;
import io.github.util.http.RestTemplateUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by 思伟 on 2020/1/9
 */
public class _Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException {
        // 要想继承实现@RequestMapping和@ResponseBody 父类的访问修饰符必须是public，不然获取到的方法和实际的方法不一致
        Method testString = Class.forName("io.github.controller.admin.SysLoginController").getMethod("testString", Model.class);
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
        System.out.println(RestTemplateUtil.postForObject("https://www.gerensuodeshui.cn/", null, String.class, 2000));


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

}
