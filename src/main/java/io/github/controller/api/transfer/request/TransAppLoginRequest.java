package io.github.controller.api.transfer.request;

import lombok.Data;

/**
 * 应用请求对象
 *
 * @author Created by 思伟 on 2020/8/6
 */
@Data
public class TransAppLoginRequest extends TransAppBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * accessToken
     */
    private String accessToken;
}