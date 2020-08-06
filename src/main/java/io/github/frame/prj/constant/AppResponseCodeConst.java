package io.github.frame.prj.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误代码定义
 * <p>
 * <br/> 错误代码8位，每两位一个节点，取值为00-ZZ（英文字母均为大写）
 * <br/> 每两位代码的含义，系统-模块-子模块-错误点，
 * <br/> 平台系统代码定义为RX；应用系统代码定义为RX，模块定义为AP，即RXAP开头
 *
 * @author Updated by 思伟 on 2019/11/27
 */
public class AppResponseCodeConst extends ResponseCodeConst {

    /**
     * 错误代码消息集合
     */
    public static final Map<String, String> errMsgMap = new HashMap<String, String>();

    /********** 系统基础异常代码 **********/
    public static final String ERROR_ILLEGAL_REQUEST = "00000001"; // 请求非法
    public static final String ERROR_NEED_SERVICE = "00000002"; // 请求类型不能为空
    public static final String ERROR_NEED_APPID = "00000003"; // 服务商编号不能为空
    public static final String ERROR_NEED_SIGN = "00000004"; // 请求数字签名不能为空
    public static final String ERROR_NEED_RANDOM = "00000005"; // 请求随机码不能为空
    public static final String ERROR_SIGN = "00000006"; // 数字签名校验错误
    public static final String ERROR_XML_PARSE = "00000007"; // XML格式错误
    public static final String ERROR_SERVICE_NOT_FOUND = "00000008"; // 请求类型未定义
    public static final String ERROR_NEED_LOGIN = "00000009"; // 请登录后再使用本功能
    public static final String ERROR_LOGOUT = "00000010"; // 您已登出系统，请重新登录
    public static final String ERROR_ILLEGAL_PARAMS = "00000011"; // 参数格式或类型错误
    public static final String ERROR_UNAUTHORIZED = "00000012"; // 您的角色不允许进行此操作
    public static final String ERROR_VERSION_EXPIRED = "00000014"; // 您的版本太旧，请更新后再使用
    public static final String ERROR_NEED_VERSION = "00000015"; // 应用版本不能为空
    public static final String ERROR_NEED_HOSID = "00000016"; // 请求机构id不能为空
    public static final String ERROR_HOSID_NOT_EXIST = "00000017"; // 请求机构不存在

    static {
        errMsgMap.put(ERROR_SYSTEM, "系统错误");
        errMsgMap.put(ERROR_VALIDATE, "数据验证不通过");
        errMsgMap.put(ERROR_ILLEGAL_REQUEST, "请求非法");
        errMsgMap.put(ERROR_NEED_SERVICE, "请求类型不能为空");
        errMsgMap.put(ERROR_NEED_APPID, "appid不能为空");
        errMsgMap.put(ERROR_NEED_SIGN, "请求数字签名不能为空");
        errMsgMap.put(ERROR_NEED_RANDOM, "请求随机码不能为空");
        errMsgMap.put(ERROR_SIGN, "数字签名校验错误");
        errMsgMap.put(ERROR_XML_PARSE, "XML格式错误");
        errMsgMap.put(ERROR_SERVICE_NOT_FOUND, "请求类型未定义");
        errMsgMap.put(ERROR_NEED_LOGIN, "请登录后再使用本功能");
        errMsgMap.put(ERROR_LOGOUT, "您已登出系统，请重新登录");
        errMsgMap.put(ERROR_ILLEGAL_PARAMS, "参数格式或类型错误");
        errMsgMap.put(ERROR_UNAUTHORIZED, "您的角色不允许进行此操作");
        errMsgMap.put(ERROR_VERSION_EXPIRED, "您的版本太旧，请更新后再使用");
        errMsgMap.put(ERROR_NEED_VERSION, "应用版本不能为空");
        errMsgMap.put(ERROR_NEED_HOSID, "请求机构id不能为空");
        errMsgMap.put(ERROR_HOSID_NOT_EXIST, "请求机构不存在");
    }

    /********** 自定义全局异常代码 **********/
    public static final String ERROR_SYS_DEMO = "RXA10000"; // 异常示例

}
