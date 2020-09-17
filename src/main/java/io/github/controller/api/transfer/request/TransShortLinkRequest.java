package io.github.controller.api.transfer.request;

import io.github.frame.prj.transfer.request.TransBaseRequest;
import lombok.Data;

/**
 * 短链接请求对象
 *
 * @author Created by 思伟 on 2020/9/11
 */
@Data
public class TransShortLinkRequest extends TransBaseRequest {

    /**
     * 前进模式
     */
    private String forwardMode;

    /**
     * 原链接
     */
    private String target;

    /**
     * 短链接
     */
    private String shortLink;

}
