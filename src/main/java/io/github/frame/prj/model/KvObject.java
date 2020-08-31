package io.github.frame.prj.model;

/**
 * 键值对对象
 *
 * @author Created by 思伟 on 2020/8/28
 */
public class KvObject {

    /**
     * 键
     */
    private String k;

    /**
     * 值
     */
    private String v;

    public KvObject() {
        super();
    }

    public KvObject(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
