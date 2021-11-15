/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.Follow;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.FollowRepository;
import me.ixk.hoshi.ums.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 关注控制器
 *
 * @author Otstar Lin
 * @date 2021/5/22 16:55
 */
@RestController
@RequestMapping("/follows")
@Api("关注控制器")
@RequiredArgsConstructor
public class FollowController {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @ApiOperation("获取关注列表")
    @GetMapping("/{userId:\\d+}/following")
    public ApiResult<ApiPage<User>> following(@PathVariable("userId") final Long userId, final Pageable page) {
        return ApiResult.page(
            this.followRepository.findByUserId(userId, page).map(Follow::getFollowing),
            "获取关注列表成功"
        );
    }

    @ApiOperation("被关注列表")
    @GetMapping("/{userId:\\d+}/followers")
    public ApiResult<ApiPage<User>> follower(@PathVariable("userId") final Long userId, final Pageable page) {
        return ApiResult.page(
            this.followRepository.findByFollowingId(userId, page).map(Follow::getUser),
            "获取被关注列表成功"
        );
    }

    @ApiOperation("添加关注")
    @PostMapping("/{followId:\\d+}")
    @PreAuthorize("hasAuthority('FOLLOW_USER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> add(@PathVariable("followId") final Long id, @Autowired final User user) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.notFound("关注失败（关注的用户不存在）").build();
        }
        final User following = optional.get();
        if (this.followRepository.findByUserIdAndFollowingId(user.getId(), following.getId()).isEmpty()) {
            // 用户关注计数 + 1
            user.setFollowingCount(user.getFollowingCount() + 1);
            following.setFollowersCount(following.getFollowersCount() + 1);
            this.userRepository.save(user);
            this.userRepository.save(following);
            // 插入关注表
            this.followRepository.save(Follow.builder().user(user).following(following).build());
        }
        return ApiResult.ok("关注成功").build();
    }

    @ApiOperation("取消关注")
    @DeleteMapping("/{followId:\\d+}")
    @PreAuthorize("hasAuthority('FOLLOW_USER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> delete(@PathVariable("followId") final Long id, @Autowired final User user) {
        this.followRepository.findByUserIdAndFollowingId(user.getId(), id)
            .ifPresent(follow -> {
                // 用户计数 - 1
                User user1 = follow.getUser();
                user1.setFollowingCount(user.getFollowingCount() - 1);
                User following = follow.getFollowing();
                following.setFollowersCount(following.getFollowersCount() - 1);
                this.userRepository.save(user1);
                this.userRepository.save(following);
                // 删除关注表
                this.followRepository.deleteById(follow.getId());
            });
        return ApiResult.ok("取消关注成功").build();
    }
}
