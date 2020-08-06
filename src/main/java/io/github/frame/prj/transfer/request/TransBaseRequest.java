package io.github.frame.prj.transfer.request;

import io.github.frame.prj.model.TokenObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求对象基类
 *
 * @author Created by 思伟 on 2020/8/3
 */
@Data
public class TransBaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 应用id */
    private String appid;
    /** 服务商渠道 */
    private String channel;
    /** 服务商渠道版本 */
    private String version;
    /** 接口名称 */
    private String service;
    /** 校验码 */
    private String sign;
    /** 随机码 */
    private String random;
    /** 返回数据格式 默认JSON */
    private String format;
    /** token值 */
    private String token;
    /** ip地址 */
    private String ip;
    /** 操作时间 */
    private Date operDate = new Date();
    /**
     * 客户端传入服务器的值，在请求响应中会返回这个值到客户端
     * 比如在android端发送会话的时候，使用异步处理，将线程id传入服务器
     * 服务器处理完成后将客户端传入的线程id同步返回到客户端
     */
    private String clientStr;
    /** 微信openid */
    private String openid;
    /** 平台医院id */
    private String companyId;

    /** token对象 */
    private TokenObject tobj;

}
