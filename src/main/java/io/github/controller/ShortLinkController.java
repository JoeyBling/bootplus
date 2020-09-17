package io.github.controller;

import io.github.frame.prj.model.ShortLinkVO;
import io.github.service.IShortLinkService;
import io.github.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短链接转发控制器
 *
 * @author Created by 思伟 on 2020/9/7
 */
@Controller
@Slf4j
public class ShortLinkController {
    /**
     * Mapping path
     */
    public final static String PATH = "/sl";

    @Autowired
    private IShortLinkService shortLinkService;

    /**
     * 渲染器
     *
     * @param shortStr 加密后的字符串
     * @param req      HttpServletRequest
     * @param resp     HttpServletResponse
     * @return DATA
     */
    @RequestMapping(PATH + "/{shortStr}")
    public Object render(@PathVariable("shortStr") String shortStr, Model model,
                         HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (StringUtils.isEmpty(shortStr)) {
            return null;
        }
        final ShortLinkVO shortLink = shortLinkService.getShortLink(shortStr);
        if (null == shortLink) {
            // 错误获取或缓存已经失效
            resp.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
            return null;
        }
        // 开始处理(后续抽离接口`getForwardMode()`)
        switch (shortLink.getForwardMode()) {
            case REDIRECT:
                return redirect(model, req, resp, shortLink);
            case GHOST:
                return ghost(model, req, resp, shortLink);
            default:
                log.warn("未处理的短链接前进模式[{}]", shortLink.getForwardMode());
                return null;
        }
    }

    /**
     * 301重定向
     */
    private Object redirect(Model model, HttpServletRequest req, HttpServletResponse resp, ShortLinkVO shortLink) {
        // use 301 than 302
        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        resp.addHeader("Location", shortLink.getTarget());
//        return "redirect:" + shortLink.getTarget();
//        resp.sendRedirect(shortLink.getTarget());
        return null;
    }

    /**
     * 伪装转发
     */
    private Object ghost(Model model, HttpServletRequest req, HttpServletResponse resp, ShortLinkVO shortLink) {
        model.addAttribute("link", shortLink);
        return PATH.concat("/ghost_default");
    }

}

