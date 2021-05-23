package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonParam;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.view.PublicUserView;
import me.ixk.hoshi.user.entity.Follow;
import me.ixk.hoshi.user.entity.User;
import me.ixk.hoshi.user.repository.FollowRepository;
import me.ixk.hoshi.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 关注控制器
 *
 * @author Otstar Lin
 * @date 2021/5/22 16:55
 */
@RestController
@RequestMapping("/api/follows")
@Api("关注控制器")
@RequiredArgsConstructor
public class FollowController {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @ApiOperation("获取关注列表")
    @GetMapping("/following")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<List<PublicUserView>> following(@ModelAttribute final User user) {
        return ApiResult.ok(
            user.getFollowing().stream().map(Follow::getFollowing).map(PublicUserView::of).collect(Collectors.toList())
        );
    }

    @ApiOperation("被关注列表")
    @GetMapping("/follower")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<List<PublicUserView>> follower(@ModelAttribute final User user) {
        return ApiResult.ok(
            user.getFollowers().stream().map(Follow::getFollower).map(PublicUserView::of).collect(Collectors.toList())
        );
    }

    @ApiOperation("添加关注")
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> add(@JsonParam("user") final Long id, @ModelAttribute final User user) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "关注失败（关注的用户不存在）" });
        }
        final User following = optional.get();
        if (this.followRepository.findByFollowerAndFollowing(user, following).isEmpty()) {
            this.followRepository.save(Follow.builder().follower(user).following(following).build());
        }
        return ApiResult.ok("关注成功").build();
    }

    @ApiOperation("取消关注")
    @DeleteMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> delete(@RequestParam("user") final Long id, @ModelAttribute final User user) {
        final Optional<User> following = this.userRepository.findById(id);
        if (following.isEmpty()) {
            return ApiResult.bindException(new String[] { "要取消关注的用户不存在" });
        }
        this.followRepository.findByFollowerAndFollowing(user, following.get())
            .ifPresent(follow -> this.followRepository.deleteById(follow.getId()));
        return ApiResult.ok("取消关注成功").build();
    }
}
