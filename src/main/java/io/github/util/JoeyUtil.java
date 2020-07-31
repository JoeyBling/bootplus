package io.github.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义工具类
 *
 * @author Created by 思伟 on 2020/6/6
 * @see DateUtils
 * @see RegexUtil
 */
@Deprecated
public class JoeyUtil {

    /**
     * 转换格式数组
     */
    @Deprecated
    static SimpleDateFormat[] simpleDateFormat = new SimpleDateFormat[10];

    /**
     * 静态初始化
     */
    static {
        simpleDateFormat[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat[1] = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat[2] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        simpleDateFormat[3] = new SimpleDateFormat("yyyy/MM/dd");
        simpleDateFormat[4] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        simpleDateFormat[5] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        simpleDateFormat[6] = new SimpleDateFormat("yyyy年/MM月/dd日");
        simpleDateFormat[7] = new SimpleDateFormat("yyyy年MM月dd日");
        simpleDateFormat[8] = new SimpleDateFormat("yyyy年-MM月-dd日");
        simpleDateFormat[9] = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒");
    }

    /**
     * 正则表达式验证
     *
     * @param regex 正则表达式
     * @param input 字符串
     * @return 返回是否符合正则表达式
     */
    public static boolean regex(String regex, CharSequence input) {
        if (null == input) {
            return false;
        }
        boolean flag = true;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (!m.find()) {
            flag = false;
        }
        return flag;
    }

    /**
     * ToByteArray
     *
     * @param obj Object
     * @return byte[]
     */
    public static byte[] toByteArray(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * ToObject
     *
     * @param bytes byte[]
     * @return Object
     */
    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return ois.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw ex;
        }
    }

}
