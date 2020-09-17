package io.github.service.impl;

import cn.hutool.crypto.SecureUtil;
import io.github.config.ApplicationProperties;
import io.github.config.aop.service.BaseAopContext;
import io.github.controller.ShortLinkController;
import io.github.frame.prj.enums.ShortLinkForwardModeEnum;
import io.github.frame.prj.model.ShortLinkVO;
import io.github.service.IShortLinkService;
import io.github.service.hessian.IExtShortLinkService;
import io.github.util.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 短链接接口实现(基于MD5码)
 * 如果用数据库肯定影响效率，所以暂时目前通过缓存实现，后续可以考虑ES
 *
 * @author Created by 思伟 on 2020/9/7
 */
@Service
public class ShortLinkService extends BaseAopContext<ShortLinkService>
        implements IExtShortLinkService, IShortLinkService {
    /**
     * 默认缓存名:建议至少1天以上有效期
     */
    private final String cacheNames = "SHORT_LINK";
    /**
     * 自定义生成`MD5`加密字符传前的混合 KEY
     */
    private final String key = "K_PRO_SHORT_URL";
    /**
     * 要使用生成`URL`的字符
     */
    private final String[] CHARS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * 缓存前缀
     */
    public String getCachePrefix() {
        return cacheNames + "_";
    }

    @Override
    public ShortLinkVO generateShortLink(ShortLinkForwardModeEnum forwardMode, String longUrl) {
        Assert.notNull(forwardMode, "必须指定短链接前进模式");
        Assert.hasText(longUrl, "长链接不能为空");
        final ShortLinkVO linkVO = new ShortLinkVO();
        linkVO.setForwardMode(forwardMode);
        final String shortStr = getShortStr(longUrl);
        // shortStr短链接字符串可能会重复
        final String shortUrl = HttpUtil.generateHttpUrl(applicationProperties.getDomain(),
                ShortLinkController.PATH, shortStr);
        linkVO.setShortLink(shortUrl);
        linkVO.setTarget(longUrl);
        return self.addShortLink(shortStr, linkVO);
    }

    /**
     * 插入短链接对象
     *
     * @param shortStr 短链接字符串
     * @param linkVO   短链接对象
     * @return ShortLinkVO
     */
    @CachePut(cacheNames = cacheNames, key = "#root.target.cachePrefix + #shortStr")
    public ShortLinkVO addShortLink(String shortStr, ShortLinkVO linkVO) {
        return linkVO;
    }

    @Override
    @Cacheable(cacheNames = cacheNames, key = "#root.target.cachePrefix + #shortStr",
            unless = "#result == null")
    public ShortLinkVO getShortLink(String shortStr) {
        logger.debug("Error shortStr[{}] to get ShortLinkVO...", shortStr);
        return null;
    }

    /**
     * 根据长链接生成短链接字符串
     *
     * @param longUrl 长链接
     * @return 加密后的字符串
     */
    public String getShortStr(String longUrl) {
        int len = 2;
        // 对传入网址进行`MD5`加密
        final String md5HexStr = SecureUtil.md5(key + longUrl);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            // substring为左闭右开
            String tempSubStr = md5HexStr.substring(i * 8, (i + 1) * 8);
            // 这里需要使用 long 型来转换，因为 Integer.parseInt() 只能处理 31 位, 首位为符号位, 如果不用 long ，则会越界
            long hexLong = 0x3FFFFFFF & Long.parseLong(tempSubStr, 16);
            String outChars = "";
            for (int j = 0; j < len; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & hexLong;
                // 把取得的字符相加
                outChars += CHARS[(int) index];
                // 每次循环按位右移 5 位
                hexLong = hexLong >> 5;
            }
            sb.append(outChars);
        }
        return sb.toString();
    }

}
