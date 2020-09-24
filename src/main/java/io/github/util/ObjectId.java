package io.github.util;

import com.google.common.base.Objects;
import io.github.frame.prj.exception.RRException;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>A globally unique identifier for objects.</p>
 * <p/>
 * <p>Consists of 12 bytes, divided as follows:</p>
 * <table border="1">
 * <caption>ObjectID layout</caption>
 * <tr>
 * <td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td>
 * </tr>
 * <tr>
 * <td colspan="4">time</td><td colspan="3">machine</td> <td colspan="2">pid</td><td colspan="3">inc</td>
 * </tr>
 * </table>
 * <p/>
 * <p>Instances of this class are immutable.</p>
 *
 * <a href="http://blog.csdn.net/odailidong/article/details/50679822">参考</a>
 *
 * @author Created by 思伟 on 2020/8/21
 */
public class ObjectId implements Comparable<ObjectId>, java.io.Serializable {

    private final int _time;
    private final int _machine;
    private final int _inc;
    private boolean _new;
    private static final int _genmachine;

    private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());

    private static final long serialVersionUID = -4415279469780082174L;

    private static final Logger LOGGER = Logger.getLogger("io.github.util.ObjectId");

    /**
     * Create a new object id.
     */
    public ObjectId() {
        _time = (int) (System.currentTimeMillis() / 1000);
        _machine = _genmachine;
        _inc = _nextInc.getAndIncrement();
        _new = true;
    }

    public static String id() {
        return get().toHexString();
    }

    /**
     * Gets a new object id.
     *
     * @return the new id
     */
    public static ObjectId get() {
        return new ObjectId();
    }

    /**
     * Checks if a string could be an {@code ObjectId}.
     *
     * @param s a potential ObjectId as a String.
     * @return whether the string could be an object id
     * @throws IllegalArgumentException if hexString is null
     */
    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        }

        final int len = s.length();
        if (len != 24) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Converts this instance into a 24-byte hexadecimal string representation.
     *
     * @return a string representation of the ObjectId in hexadecimal format
     */
    public String toHexString() {
        final StringBuilder buf = new StringBuilder(24);
        for (final byte b : toByteArray()) {
            buf.append(String.format("%02x", b & 0xff));
        }
        return buf.toString();
    }

    /**
     * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     *
     * @return the byte array
     */
    public byte[] toByteArray() {
        byte b[] = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        // by default BB is big endian like we need
        bb.putInt(_time);
        bb.putInt(_machine);
        bb.putInt(_inc);
        return b;
    }

    private int _compareUnsigned(int i, int j) {
        long li = 0xFFFFFFFFL;
        li = i & li;
        long lj = 0xFFFFFFFFL;
        lj = j & lj;
        long diff = li - lj;
        if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) diff;
    }

    @Override
    public int compareTo(ObjectId id) {
        if (id == null) {
            return -1;
        }

        int x = _compareUnsigned(_time, id._time);
        if (x != 0) {
            return x;
        }

        x = _compareUnsigned(_machine, id._machine);
        if (x != 0) {
            return x;
        }

        return _compareUnsigned(_inc, id._inc);
    }

    /**
     * Gets the timestamp (number of seconds since the Unix epoch).
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return _time;
    }

    /**
     * Gets the timestamp as a {@code Date} instance.
     *
     * @return the Date
     */
    public Date getDate() {
        return new Date(_time * 1000L);
    }

    /**
     * Gets the current value of the auto-incrementing counter.
     *
     * @return the current counter value.
     */
    public static int getCurrentCounter() {
        return _nextInc.get();
    }

    static {

        try {
            // build a 2-byte machine piece based on NICs info
            int machinePiece;
            {
                try {
                    StringBuilder sb = new StringBuilder();
                    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                    while (e.hasMoreElements()) {
                        NetworkInterface ni = e.nextElement();
                        sb.append(ni.toString());
                    }
                    machinePiece = sb.toString().hashCode() << 16;
                } catch (Throwable e) {
                    // exception sometimes happens with IBM JVM, use random
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                    machinePiece = (new Random().nextInt()) << 16;
                }
                LOGGER.fine("machine piece post: " + Integer.toHexString(machinePiece));
            }

            // add a 2 byte process piece. It must represent not only the JVM but the class loader.
            // Since static var belong to class loader there could be collisions otherwise
            final int processPiece;
            {
                int processId = new Random().nextInt();
                try {
                    processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
                } catch (Throwable t) {
                }

                ClassLoader loader = ObjectId.class.getClassLoader();
                int loaderId = loader != null ? System.identityHashCode(loader) : 0;

                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toHexString(processId));
                sb.append(Integer.toHexString(loaderId));
                processPiece = sb.toString().hashCode() & 0xFFFF;
                LOGGER.fine("process piece: " + Integer.toHexString(processPiece));
            }

            _genmachine = machinePiece | processPiece;
            LOGGER.fine("machine : " + Integer.toHexString(_genmachine));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("static-access")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectId that = (ObjectId) o;

        return Objects.equal(serialVersionUID, serialVersionUID) &&
                Objects.equal(LOGGER, LOGGER) &&
                Objects.equal(this._time, that._time) &&
                Objects.equal(this._machine, that._machine) &&
                Objects.equal(this._inc, that._inc) &&
                Objects.equal(this._new, that._new) &&
                Objects.equal(_nextInc, _nextInc) &&
                Objects.equal(_genmachine, _genmachine);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serialVersionUID, LOGGER, _time, _machine, _inc, _new,
                _nextInc, _genmachine);
    }

    /**
     * 安全随机数实现
     */
    private static Random random = new SecureRandom();
    /**
     * 唯一值long的最大长度
     */
    private static final int LONG_MAX_LEN = 18;

    /**
     * 唯一的long型数字id
     *
     * @return long
     */
    public static long uniqId() {
        String nanoRandom = System.nanoTime() + "" + random.nextInt(99999);
        int hash = Math.abs(UUID.randomUUID().hashCode());
        int needAddLen = LONG_MAX_LEN - String.valueOf(hash).length();
        // 下标需从1开始（最大长度为18）
        return Long.valueOf(StringUtils.left(hash + "" + nanoRandom.substring(0, needAddLen), LONG_MAX_LEN));
    }

    public static void main(String[] args) {
        // 线程池
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5,
                15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));
        final int initialCapacity = 1000;
        List<Long> longList = new ArrayList<>(initialCapacity);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                for (int y = 0; y < initialCapacity; y++) {
                    System.out.println(id());
                    final long uniqId = uniqId();
                    if (longList.contains(uniqId)) {
                        throw new RRException("出现重复值");
                    }
                    longList.add(uniqId);
                    System.out.println(uniqId);
                }
                System.out.println("test finished...");
                System.out.println(longList.size());
            });
        }

    }

}