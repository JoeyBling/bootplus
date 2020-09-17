package io.github.service;

import io.github.base.BaseAppTest;
import io.github.frame.prj.model.ShortLinkVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 短链接接口实现Test
 *
 * @author Created by 思伟 on 2020/9/7
 */
public class ShortLinkServiceTest extends BaseAppTest {

    @Autowired
    private IShortLinkService shortLinkService;

    @Test
    public void generateShortLink() {
        for (int i = 0; i < 100; i++) {
            final String longUrl = "https://my.freenom.com/clientarea.php?action=domaindetails";
            final ShortLinkVO linkVO = shortLinkService.generateShortLink(
                    longUrl);
            log.debug("{}", linkVO);
        }
    }

    @Test
    public void getShortLink() {
        final String longUrl = "https://my.freenom.com/clientarea.php?action=domaindetails";
        final ShortLinkVO linkVO = shortLinkService.generateShortLink(
                longUrl);
        final ShortLinkVO shortLink = shortLinkService.getShortLink("6vnmuM77");
        log.debug("{}", shortLink);
    }

}