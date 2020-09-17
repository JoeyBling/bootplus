package io.github.frame.prj.model;

import io.github.frame.prj.enums.ShortLinkForwardModeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 短链接VO对象
 *
 * @author Created by 思伟 on 2020/9/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortLinkVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短链接
     */
    private String shortLink;

    /**
     * 目标地址 Is it better to use `url`?
     */
    private String target;

    /**
     * 前进模式
     */
    private ShortLinkForwardModeEnum forwardMode;

}
