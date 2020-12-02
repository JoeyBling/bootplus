package io.github.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.sql.Timestamp;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 改进 {@link cn.hutool.core.date.SystemClock}
 * 高并发场景下`System.currentTimeMillis()`的性能问题的优化
 * <p><p>
 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右）<p>
 * System.currentTimeMillis()之所以慢是因为去跟系统打了一次交道<p>
 * 后台定时更新时钟，JVM退出时，线程自动回收<p>
 * 10亿：43410,206,210.72815533980582%<p>
 * 1亿：4699,29,162.0344827586207%<p>
 * 1000万：480,12,40.0%<p>
 * 100万：50,10,5.0%<p>
 *
 * @author lry
 * @author Updated by 思伟 on 2020/10/10
 */
public class SystemClock {

    /**
     * 延迟执行的时间(单位：毫秒)
     */
    private final long period;
    /**
     * 原子对象
     * 对长整形进行原子操作
     * `volatile`不能保证原子性而`Atomic`可以 @url { https://blog.csdn.net/weixin_34190136/article/details/86275092 }
     */
    private final AtomicLong now;

    /**
     * 默认延迟为1毫秒
     */
    private SystemClock() {
        this(1);
    }

    /**
     * 外部不提供实例化方法
     */
    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 获取唯一实例对象
     */
    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 获取当前时间戳(毫秒级别)
     *
     * @see #currentTimeMillis()
     */
    public static long now() {
        return instance().currentTimeMillis();
    }

    /**
     * 获得时间戳对象 {@link Timestamp}
     *
     * @return Timestamp
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(now());
    }

    /**
     * 获得JDBC格式的时间戳 {@link #now()}
     * Formats a timestamp in JDBC timestamp escape format.
     *
     * @return String
     */
    public static String nowDate() {
        return nowTimestamp().toString();
    }

    /**
     * 定时更新时间戳
     */
    private void scheduleClockUpdating() {
        // 核心线程数为1即可
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder()
                .setNameFormat("System-Clock-%d")
                // 守护进程线程
                .setDaemon(true).build());
        // 按指定频率周期执行任务
        scheduler.scheduleAtFixedRate(() ->
                // 设置时间戳
                now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取当前时间戳(毫秒级别)
     *
     * @return long
     */
    private long currentTimeMillis() {
        // 获取当前值
        return now.get();
    }

    /**
     * 登记式/静态内部类
     */
    private static class InstanceHolder {
        public static final SystemClock INSTANCE = new SystemClock();
    }

    /**
     * just test
     */
    public static void main(String[] args) {
        for (int i = 0; i < 5000; i++) {
            System.out.println(now());
            System.out.println(nowTimestamp());
            System.out.println(nowDate());
        }
    }
}
