package io.github.frame.prj.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户类型常量
 *
 * @author Created by 思伟 on 2020/8/3
 */
public class UserTypeConst {

    /**
     * 用户类型-系统(系统动作，机器人)
     */
    public static final String SYS = "SYS";
    /**
     * 用户类型-管理员(管理后台用户)
     */
    public static final String ADM = "ADM";
    /**
     * 用户类型-应用端用户
     */
    public static final String APP = "APP";

    /**
     * 常量集合
     */
    public static final Map<String, String> CONST_MAP = new HashMap<String, String>();

    static {
        CONST_MAP.put(SYS, "系统");
        CONST_MAP.put(ADM, "管理员");
        CONST_MAP.put(APP, "应用端用户");
    }

}