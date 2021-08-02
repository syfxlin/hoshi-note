package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公开用户控制器
 *
 * @author Otstar Lin
 * @date 2021/7/31 14:26
 */
@RestController
@RequestMapping("/api/users")
@Api("公开用户控制器")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserRepository userRepository;

    @ApiOperation("获取用户")
    @GetMapping("/{username}")
    public ApiResult<Object> user(
        @PathVariable("username") final String username
    ) {
        final Optional<User> byUsername = userRepository.findByUsername(
            username
        );
        if (byUsername.isEmpty()) {
            return ApiResult.bindException("用户不存在");
        }
        return ApiResult.ok(byUsername.get(), "获取用户成功");
    }
}
