package io.github.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.AbstractTemplateView;

/**
 * 系统管理员页面视图
 * `@PathVariable`尽量别使用spring关键字
 * yml:allow-request-override
 * 当这里的方法报错时，程序会报以下错误，待解决
 * TODO Cannot expose session attribute 'user' because of an existing model object of the same name
 *
 * @author Created by 思伟 on 2020/6/6
 * @see io.github.frame.controller.MySiteErrorController#errorHtml
 * @see AbstractTemplateView#renderMergedOutputModel
 */
@Controller
public class SysPageController {

    @RequestMapping("/admin/{path}/{url}.html")
    public String page(@PathVariable("path") String path, @PathVariable("url") String url) {
        return "/admin/" + path + "/" + url;
    }

    @RequestMapping("/admin/{url}.html")
    public String pageUrl(@PathVariable("url") String url) {
        return "/admin/" + url;
    }

}
