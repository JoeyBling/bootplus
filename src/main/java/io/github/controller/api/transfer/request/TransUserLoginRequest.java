package io.github.controller.api.transfer.request;

import lombok.Data;

/**
 * 用户请求对象
 *
 * @author Created by 思伟 on 2020/8/4
 */
@Data
public class TransUserLoginRequest extends TransAppBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    private Long adminId;

    /**
     * 登录IP(筛选模糊查询)
     */
    private String loginIp;

}