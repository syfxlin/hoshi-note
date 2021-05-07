/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.ums.service.UserTestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Otstar Lin
 * @date 2021/5/2 上午 10:50
 */
@RestController
@Api("测试控制器")
@RequiredArgsConstructor
public class IndexController {

    private final UserTestService userTestService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "管理员权限测试", authorizations = @Authorization("admin"))
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "用户权限测试", authorizations = @Authorization("user"))
    public String user() {
        return "user";
    }

    @GetMapping("/user/ip")
    @PreAuthorize("hasRole('USER')")
    public int user2(final HttpServletRequest request) {
        return request.getServerPort();
    }
}
