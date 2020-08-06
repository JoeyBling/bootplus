package io.github.controller.api.transfer.request;

import io.github.frame.prj.transfer.request.TransPageBaseRequest;
import lombok.Data;

/**
 * 应用请求对象
 *
 * @author Created by 思伟 on 2020/8/4
 */
@Data
public class TransAppBaseRequest extends TransPageBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

}