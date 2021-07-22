/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mail.entity.VerifyCode;
import me.ixk.hoshi.mail.service.VerifyCodeService;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.RoleRepository;
import me.ixk.hoshi.ums.repository.TokenRepository;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.view.request.RegisterUserView;
import me.ixk.hoshi.ums.view.request.ResetPasswordView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 权限控制器
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 4:53
 */
@RestController
@RequiredArgsConstructor
@Api("权限控制器")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final VerifyCodeService verifyCodeService;

    @ApiOperation("注册")
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> register(@Valid @JsonModel final RegisterUserView vo) {
        if (this.userRepository.findByUsername(vo.getUsername()).isPresent()) {
            return ApiResult.bindException("用户名已存在");
        }
        final User user = User.ofRegister(vo);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(this.roleRepository.findById("USER").get()));
        return ApiResult.ok(this.userRepository.save(user), "注册成功");
    }

    @ApiOperation("Token 登录")
    @PostMapping("/api/login")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> apiLogin(@RequestParam("token") final String token) {
        final Optional<Token> tokenOptional = this.tokenRepository.findById(token);
        if (tokenOptional.isEmpty()) {
            return ApiResult.unauthorized("Token 无效").build();
        }
        final User user = tokenOptional.get().getUser();
        final UserDetails details = new UserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream().filter(Role::getStatus).map(Role::getName).collect(Collectors.toList()),
            user.getStatus()
        );
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            details,
            null,
            details.getAuthorities()
        );
        final Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setDetails(currentAuthentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ApiResult.ok(user, "登录成功");
    }

    @ApiOperation("发送找回密码验证码")
    @PostMapping("/api/reset-password/{email}")
    @PreAuthorize("isAnonymous()")
    public ApiResult<Object> sendResetPasswordCode(
        @PathVariable("email") final String email,
        final HttpServletRequest request
    ) {
        final Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            return ApiResult.bindException("指定邮箱用户不存在");
        }
        verifyCodeService.generate(
            email,
            "找回密码",
            60 * 30,
            request.getRemoteAddr(),
            e -> UUID.randomUUID().toString()
        );
        return ApiResult.ok("验证码发送成功").build();
    }

    @ApiOperation("找回密码")
    @PutMapping("/api/reset-password")
    @PreAuthorize("isAnonymous()")
    public ApiResult<Object> resetPassword(@Valid @JsonModel final ResetPasswordView vo) {
        final Optional<VerifyCode> verifyCode = verifyCodeService.find("找回密码", vo.getCode());
        if (verifyCode.isEmpty()) {
            return ApiResult.bindException("验证码无效");
        }
        final Optional<User> byEmail = userRepository.findByEmail(verifyCode.get().getEmail());
        if (byEmail.isEmpty()) {
            return ApiResult.bindException("指定邮箱用户不存在");
        }
        final User user = byEmail.get();
        user.setPassword(passwordEncoder.encode(vo.getPassword()));
        return ApiResult.ok(userRepository.save(user), "重置密码成功");
    }
}
