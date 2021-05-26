package me.ixk.hoshi.ums.controller;

import static me.ixk.hoshi.security.util.Security.USER_ATTR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.db.entity.Follow;
import me.ixk.hoshi.db.entity.User;
import me.ixk.hoshi.db.repository.FollowRepository;
import me.ixk.hoshi.db.repository.UserRepository;
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
    @GetMapping("/{userId}/following")
    public ApiResult<Object> following(@PathVariable("userId") final String userId) {
        return ApiResult.ok(
            this.followRepository.findByFollowerId(userId)
                .stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList()),
            "获取关注列表成功"
        );
    }

    @ApiOperation("被关注列表")
    @GetMapping("/{userId}/follower")
    public ApiResult<Object> follower(@PathVariable("userId") final String userId) {
        return ApiResult.ok(
            this.followRepository.findByFollowingId(userId)
                .stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList())
        );
    }

    @ApiOperation("添加关注")
    @PostMapping("/{followId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> add(
        @PathVariable("followId") final String id,
        @ModelAttribute(USER_ATTR) final User user
    ) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "关注失败（关注的用户不存在）" });
        }
        final User following = optional.get();
        if (this.followRepository.findByFollowerIdAndFollowingId(user.getId(), following.getId()).isEmpty()) {
            this.followRepository.save(Follow.builder().follower(user).following(following).build());
        }
        return ApiResult.ok("关注成功").build();
    }

    @ApiOperation("取消关注")
    @DeleteMapping("/{followId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> delete(
        @PathVariable("followId") final String id,
        @ModelAttribute(USER_ATTR) final User user
    ) {
        this.followRepository.findByFollowerIdAndFollowingId(user.getId(), id)
            .ifPresent(follow -> this.followRepository.deleteById(follow.getId()));
        return ApiResult.ok("取消关注成功").build();
    }
}
