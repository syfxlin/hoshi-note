/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mail.service.VerifyCodeService;
import me.ixk.hoshi.mysql.util.Jpa;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.entity.UserInfo;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.request.UpdateEmailView;
import me.ixk.hoshi.ums.request.UpdateNameView;
import me.ixk.hoshi.ums.request.UpdatePasswordView;
import me.ixk.hoshi.ums.request.UpdateUserInfoView;
import me.ixk.hoshi.ums.response.LoggedView;
import me.ixk.hoshi.web.annotation.JsonModel;
import me.ixk.hoshi.web.details.WebAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/users")
@Api("用户控制器")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerifyCodeService verifyCodeService;

    @ApiOperation("获取用户")
    @GetMapping("")
    @PreAuthorize("hasAuthority('ME')")
    public final ApiResult<?> user(@Autowired final User user) {
        return ApiResult.ok(user.toView(), "获取当前用户成功");
    }

    @ApiOperation("通过用户名获取用户")
    @GetMapping("/{username}")
    public ApiResult<?> user(@PathVariable("username") final String username) {
        final Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            return ApiResult.bindException("用户不存在");
        }
        return ApiResult.ok(byUsername.get().toView(), "获取用户成功");
    }

    @ApiOperation("更新用户名")
    @PutMapping("/name")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<?> updateName(@Autowired final User user, @Valid @JsonModel final UpdateNameView vo) {
        final User update = User.ofUpdateName(vo, user.getId());
        return ApiResult.ok(this.userRepository.update(update).toView(), "更新用户名成功");
    }

    @ApiOperation("发送更新邮箱验证码")
    @PostMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<?> sendEmailCode(@PathVariable("email") final String email, final HttpServletRequest request) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            return ApiResult.bindException("已经有用户绑定了该邮箱");
        }
        verifyCodeService.generate(
            email,
            "验证您的邮箱账户",
            60L * 30L,
            request.getRemoteAddr(),
            RandomUtil.randomString(10)
        );
        return ApiResult.ok("验证码发送成功").build();
    }

    @ApiOperation("更新用户邮箱")
    @PutMapping("/email")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<?> updateEmail(@Autowired final User user, @Valid @JsonModel final UpdateEmailView vo) {
        if (!verifyCodeService.verify("验证您的邮箱账户", vo.getCode())) {
            return ApiResult.bindException("验证码无效");
        }
        final User update = User.ofUpdateEmail(vo, user.getId());
        return ApiResult.ok(this.userRepository.update(update).toView(), "更新邮箱成功");
    }

    @ApiOperation("更新用户密码")
    @PutMapping("/password")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<?> updatePassword(@Autowired final User user, @Valid @JsonModel final UpdatePasswordView vo) {
        if (!this.passwordEncoder.matches(vo.getOldPassword(), user.getPassword())) {
            return ApiResult.bindException("旧密码不匹配");
        }
        final User update = User.ofUpdatePassword(vo, user.getId());
        update.setPassword(passwordEncoder.encode(update.getPassword()));
        return ApiResult.ok(this.userRepository.update(update).toView(), "更新密码成功");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<?> getInfo(@Autowired final User user) {
        return ApiResult.ok(user.getInfo().toView(), "获取当前用户信息成功");
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    @PreAuthorize("hasAuthority('ME')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> updateInfo(@Autowired final User user, @Valid @JsonModel final UpdateUserInfoView vo) {
        user.setInfo(Jpa.merge(UserInfo.ofUpdate(vo, user.getInfo().getId()), user.getInfo()));
        return ApiResult.ok(this.userRepository.save(user).getInfo().toView(), "更新用户成功");
    }

    @ApiOperation("获取当前用户登录信息")
    @GetMapping("/logged")
    @PreAuthorize("hasAuthority('ME_LOGGED_MANAGER')")
    public ApiResult<?> logged(@Autowired final User user) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        return ApiResult.ok(
            this.sessionRepository.findByPrincipalName(user.getUsername())
                .entrySet()
                .stream()
                .map(e -> {
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
                        .current(e.getKey().equals(sessionId))
                        .build();
                })
                .sorted(Comparator.comparing(LoggedView::getCreationTime).reversed())
                .collect(Collectors.toList()),
            "获取当前用户登录信息成功"
        );
    }

    @ApiOperation("踢出用户")
    @DeleteMapping("/exclude/{sessionId}")
    @PreAuthorize("hasAuthority('ME_LOGGED_MANAGER')")
    public ApiResult<?> exclude(@Autowired final User user, @PathVariable("sessionId") final String sessionId) {
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
