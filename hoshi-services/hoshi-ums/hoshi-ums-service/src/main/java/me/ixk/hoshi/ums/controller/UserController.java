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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mail.service.VerifyCodeService;
import me.ixk.hoshi.mysql.util.Jpa;
import me.ixk.hoshi.security.security.WebAuthenticationDetails;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.entity.UserInfo;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.view.request.UpdateEmailView;
import me.ixk.hoshi.ums.view.request.UpdateNameView;
import me.ixk.hoshi.ums.view.request.UpdatePasswordView;
import me.ixk.hoshi.ums.view.request.UpdateUserInfoView;
import me.ixk.hoshi.ums.view.response.LoggedView;
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
@RequestMapping("/api/users")
@Api("用户控制器")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserRepository userRepository;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerifyCodeService verifyCodeService;

    @ApiOperation("获取用户")
    @GetMapping("")
    public final ApiResult<Object> user(@Autowired final User user) {
        return ApiResult.ok(user, "获取当前用户成功");
    }

    @ApiOperation("更新用户名")
    @PutMapping("/name")
    public ApiResult<Object> updateName(@Autowired final User user, @Valid @JsonModel final UpdateNameView vo) {
        final User update = User.ofUpdateName(vo, user.getId());
        return ApiResult.ok(this.userRepository.update(update), "更新用户名成功");
    }

    @ApiOperation("发送更新邮箱验证码")
    @GetMapping("/email/code")
    public ApiResult<Object> sendEmailCode(@Autowired final User user, final HttpSession session) {
        verifyCodeService.generate(user.getEmail(), "验证您的邮箱账户", session);
        return ApiResult.ok("验证码发送成功").build();
    }

    @ApiOperation("获取用户邮箱")
    @PutMapping("/email")
    public ApiResult<Object> updateEmail(
        @Autowired final User user,
        @Valid @JsonModel final UpdateEmailView vo,
        final HttpSession session
    ) {
        if (!verifyCodeService.verify("验证您的邮箱账户", vo.getCode(), session)) {
            return ApiResult.bindException("验证码不匹配");
        }
        final User update = User.ofUpdateEmail(vo, user.getId());
        return ApiResult.ok(this.userRepository.update(update), "更新邮箱成功");
    }

    @ApiOperation("获取用户密码")
    @PutMapping("/password")
    public ApiResult<Object> updatePassword(@Autowired final User user, @Valid @JsonModel final UpdatePasswordView vo) {
        if (!this.passwordEncoder.matches(vo.getOldPassword(), user.getPassword())) {
            return ApiResult.bindException("旧密码不匹配");
        }
        final User update = User.ofUpdatePassword(vo, user.getId());
        update.setPassword(passwordEncoder.encode(update.getPassword()));
        return ApiResult.ok(this.userRepository.update(update), "更新密码成功");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public ApiResult<UserInfo> getInfo(@Autowired final User user) {
        return ApiResult.ok(user.getInfo(), "获取当前用户信息成功");
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<UserInfo> updateInfo(@Autowired final User user, @Valid @JsonModel final UpdateUserInfoView vo) {
        user.setInfo(Jpa.merge(UserInfo.ofUpdate(vo, user.getInfo().getId()), user.getInfo()));
        return ApiResult.ok(this.userRepository.save(user).getInfo(), "更新用户成功");
    }

    @ApiOperation("获取当前用户登录信息")
    @GetMapping("/logged")
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
