package io.github.service.hessian;

import io.github.frame.prj.enums.ShortLinkForwardModeEnum;
import io.github.frame.prj.model.ShortLinkVO;

/**
 * 短链接Hessian接口
 * mark:hessian接口默认不支持方法重载，如需开启
 * 请设置overloadEnabled属性为true
 *
 * @author Created by 思伟 on 2020/9/7
 */
public interface IExtShortLinkService {

    /**
     * 使用默认前进模式生成短链接
     *
     * @param longUrl 长链接
     * @return ShortLinkVO
     * @see #generateShortLink(ShortLinkForwardModeEnum, String)
     */
    default ShortLinkVO generateShortLink(String longUrl) {
        return this.generateShortLink(ShortLinkForwardModeEnum.DEFAULT, longUrl);
    }

    /**
     * 生成短链接
     *
     * @param forwardMode 前进模式
     * @param longUrl     长链接
     * @return ShortLinkVO
     */
    ShortLinkVO generateShortLink(ShortLinkForwardModeEnum forwardMode, String longUrl);

}
