package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.user.entity.Users;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 5:12
 */
@RestController
@RequestMapping("/users")
@Api("用户控制器")
public class UserController {

    @ApiOperation(value = "获取用户信息", authorizations = { @Authorization("admin"), @Authorization("user") })
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Users> user(@AuthenticationPrincipal final UserDetails userDetails) {
        return ApiResult.ok(userDetails.getUser());
    }
}
