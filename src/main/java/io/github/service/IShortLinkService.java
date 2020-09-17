package io.github.service;

import io.github.frame.prj.model.ShortLinkVO;
import io.github.service.hessian.IExtShortLinkService;

/**
 * 短链接接口
 *
 * @author Created by 思伟 on 2020/9/7
 */
public interface IShortLinkService extends IExtShortLinkService {

    /**
     * 根据加密后的字符串获取短链接信息
     *
     * @param shortStr 加密后的字符串
     * @return ShortLinkVO
     */
    ShortLinkVO getShortLink(String shortStr);

}
