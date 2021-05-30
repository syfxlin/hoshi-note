/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.security.WebAuthenticationDetails;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.entity.UserInfo;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.view.LoggedView;
import me.ixk.hoshi.ums.view.UserInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 用户控制器
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 5:12
 */
@RestController
@RequestMapping("/api/users")
@Api("用户控制器")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @ApiOperation("获取用户")
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ApiResult<Object> user(@Autowired final User user) {
        return ApiResult.ok(user, "获取当前用户成功");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    @PreAuthorize("hasRole('USER')")
    public ApiResult<UserInfo> getInfo(@Autowired final User user) {
        return ApiResult.ok(user.getInfo(), "获取当前用户信息成功");
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    @PreAuthorize("hasRole('USER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<UserInfo> updateInfo(@Autowired final User user, @Valid @JsonModel final UserInfoView vo) {
        final UserInfo info = user.getInfo();
        final String address = vo.getAddress();
        if (address != null) {
            info.setAddress(address);
        }
        final String avatar = vo.getAvatar();
        if (avatar != null) {
            info.setAvatar(avatar);
        }
        final String bio = vo.getBio();
        if (bio != null) {
            info.setBio(bio);
        }
        final String company = vo.getCompany();
        if (company != null) {
            info.setCompany(company);
        }
        final String status = vo.getStatus();
        if (status != null) {
            info.setStatus(status);
        }
        final String url = vo.getUrl();
        if (url != null) {
            info.setUrl(url);
        }
        return ApiResult.ok(this.userRepository.save(user).getInfo(), "更新用户成功");
    }

    @ApiOperation("获取当前用户登录信息")
    @GetMapping("/logged")
    @PreAuthorize("hasRole('USER')")
    public ApiResult<List<LoggedView>> logged(@Autowired final User user) {
        return ApiResult.ok(
            this.sessionRepository.findByPrincipalName(user.getUsername())
                .entrySet()
                .stream()
                .map(
                    e -> {
                        final Session session = e.getValue();
                        final SecurityContext securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
                        final WebAuthenticationDetails details = (WebAuthenticationDetails) securityContext
                            .getAuthentication()
                            .getDetails();
                        final OffsetDateTime creationTime = OffsetDateTime.ofInstant(
                            session.getCreationTime(),
                            ZoneId.systemDefault()
                        );
                        final OffsetDateTime lastAccessedTime = OffsetDateTime.ofInstant(
                            session.getLastAccessedTime(),
                            ZoneId.systemDefault()
                        );
                        return LoggedView
                            .builder()
                            .sessionId(e.getKey())
                            .address(details.getAddress())
                            .userAgent(details.getUserAgent())
                            .creationTime(creationTime)
                            .lastAccessedTime(lastAccessedTime)
                            .build();
                    }
                )
                .sorted(Comparator.comparing(LoggedView::getCreationTime).reversed())
                .collect(Collectors.toList()),
            "获取当前用户登录信息成功"
        );
    }

    @ApiOperation("踢出用户")
    @DeleteMapping("/exclude/{sessionId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResult<Object> exclude(@Autowired final User user, @PathVariable("sessionId") final String sessionId) {
        if (sessionId.equals(RequestContextHolder.currentRequestAttributes().getSessionId())) {
            return ApiResult.bindException("不能踢出自己");
        }
        final Session session = this.sessionRepository.findByPrincipalName(user.getUsername()).get(sessionId);
        if (session == null) {
            return ApiResult.ok("指定的登录已失效，无须踢出").build();
        }
        this.sessionRepository.deleteById(session.getId());
        return ApiResult.ok("踢出成功").build();
    }
}
