package me.ixk.hoshi.consumer.controller;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.consumer.service.UmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Otstar Lin
 * @date 2021/5/9 下午 3:10
 */
@RequiredArgsConstructor
@RestController
public class TestController {

    public final UmsService umsService;

    @GetMapping("/user")
    public String user() {
        return umsService.user();
    }
}
