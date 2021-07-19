package me.ixk.hoshi.mail.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Otstar Lin
 * @date 2021/7/18 20:34
 */
@Controller
public class TestController {

    @GetMapping("/code")
    public String code(final Model model) {
        model.addAttribute("subject", "验证您的邮箱账户");
        model.addAttribute("code", RandomUtil.randomString(6));
        model.addAttribute("helpUrl", "https://github.com/syfxlin/hoshi-note");
        model.addAttribute("logoUrl", "https://note.ixk.me/logo.png");
        return "code";
    }
}
