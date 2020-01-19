package io.github.util;

import io.github.App;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class工具类
 *
 * @author Created by 思伟 on 2020/1/10
 */
public class ClassUtil {

    /**
     * 获取包路径
     */
    public static String getPackageName(Object obj) throws Exception {
        Assert.notNull(obj, "obj must not be null");
        return getPackageName(AopTargetUtils.getTarget(obj).getClass());
    }

    public static String getPackageName(Class obj) {
        Assert.notNull(obj, "obj must not be null");
        return obj.getPackage().getName();
    }

    /**
     * 获取类名
     */
    public static String getClassName(Object obj) throws Exception {
        Assert.notNull(obj, "obj must not be null");
        return getClassName(AopTargetUtils.getTarget(obj).getClass());
    }

    public static String getClassName(Class obj) {
        Assert.notNull(obj, "obj must not be null");
        return obj.getSimpleName();
    }

    /**
     * 获取类路径
     */
    public static String getClass(Object obj) throws Exception {
        Assert.notNull(obj, "obj must not be null");
        return getClass(AopTargetUtils.getTarget(obj).getClass());
    }

    public static String getClass(Class obj) {
        Assert.notNull(obj, "obj must not be null");
        return obj.getName();
    }

    /**
     * 通过包名获取包内所有类
     *
     * @param pkg 包名
     * @return List
     */
    public static List<Class<?>> getAllClassByPackageName(Package pkg) throws IOException, ClassNotFoundException {
        String packageName = pkg.getName();
        return getAllClassByPackageName(packageName);
    }

    /**
     * 通过包名获取包内所有类
     *
     * @param packageName 包名
     * @return List
     */
    public static List<Class<?>> getAllClassByPackageName(String packageName) throws IOException, ClassNotFoundException {
        // 获取当前包以及子包下所有的类
        List<Class<?>> returnClassList = getClasses(packageName);
        return returnClassList;
    }


    /**
     * 取得某个接口下所有实现这个接口的类(默认是扫描接口类所在包以及子包)
     *
     * @param interfaceClass 接口类
     * @return List
     */
    public static List<Class<?>> getAllClassByInterface(Class<?> interfaceClass) throws IOException, ClassNotFoundException {
        List<Class<?>> returnClassList = null;
        if (null != interfaceClass && interfaceClass.isInterface()) {
            // 获取当前的包名
            String packageName = interfaceClass.getPackage().getName();
            // 获取当前包下以及子包下所以的类
            List<Class<?>> allClass = getClasses(packageName);
            if (allClass != null) {
                returnClassList = new ArrayList<Class<?>>();
                for (Class<?> cls : allClass) {
                    // 判断是否是同一个接口
                    if (interfaceClass.isAssignableFrom(cls)) {
                        // 本身不加入进去
                        if (!interfaceClass.equals(cls)) {
                            returnClassList.add(cls);
                        }
                    }
                }
            }
        }
        return returnClassList;
    }

    /**
     * 取得某一类所在包的所有类名 不含迭代
     *
     * @return String[]
     */
    public static String[] getPackageAllClassName(final String classLocation, final String packageName) {
        // 将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        for (int i = 0; i < packagePathSplit.length; i++) {
            realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
        }
        File packageDir = new File(realClassLocation);
        if (packageDir.isDirectory()) {
            String[] allClassName = packageDir.list();
            return allClassName;
        }
        return null;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName 包名
     * @return List
     */
    private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        Assert.notNull(packageName, "packageName must not be null");
        // 第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            throw e;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        throw e;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 目录路径
     * @param recursive   是否循环迭代
     * @param classes     需要添加的List
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) throws ClassNotFoundException {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirFiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirFiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                // 递归
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    throw e;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println(ClassUtil.getClass(ClassUtil.class));
//        System.out.println(ClassUtil.getClass(null));
//        List<Class<?>> classList = ClassUtil.getAllClassByPackageName(ClassUtil.class.getPackage());
        List<Class<?>> classList = ClassUtil.getAllClassByPackageName(App.scanBasePackages);
        classList.forEach(aClass -> {
            System.out.println(ClassUtil.getClass(aClass));
        });
    }
}
