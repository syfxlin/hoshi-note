package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.view.PublicUserView;
import me.ixk.hoshi.user.entity.Follow;
import me.ixk.hoshi.user.entity.User;
import me.ixk.hoshi.user.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开放用户信息控制器
 *
 * @author Otstar Lin
 * @date 2021/5/22 17:02
 */
@RestController
@RequestMapping("/api/users/{id:\\d+}")
@Api("开放用户信息控制器")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserRepository userRepository;

    @ApiOperation("获取用户信息")
    @GetMapping("")
    public ApiResult<Object> user(@PathVariable("id") final Long id) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        return ApiResult.ok(PublicUserView.of(optional.get()));
    }

    @ApiOperation("获取关注列表")
    @GetMapping("/following")
    public ApiResult<Object> following(@PathVariable("id") final Long id) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        return ApiResult.ok(
            optional
                .get()
                .getFollowing()
                .stream()
                .map(Follow::getFollowing)
                .map(PublicUserView::of)
                .collect(Collectors.toList())
        );
    }

    @ApiOperation("被关注列表")
    @GetMapping("/follower")
    public ApiResult<Object> follower(@PathVariable("id") final Long id) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        return ApiResult.ok(
            optional
                .get()
                .getFollowers()
                .stream()
                .map(Follow::getFollower)
                .map(PublicUserView::of)
                .collect(Collectors.toList())
        );
    }
}
