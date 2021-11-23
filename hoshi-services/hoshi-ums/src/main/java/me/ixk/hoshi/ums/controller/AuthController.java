/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mail.entity.VerifyCode;
import me.ixk.hoshi.mail.service.VerifyCodeService;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.RoleRepository;
import me.ixk.hoshi.ums.repository.TokenRepository;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.request.RegisterUserView;
import me.ixk.hoshi.ums.request.ResetPasswordView;
import me.ixk.hoshi.web.annotation.JsonModel;
import me.ixk.hoshi.web.details.UserDetails;
import me.ixk.hoshi.web.details.WebAuthenticationDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 验证控制器
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 4:53
 */
@RestController
@RequiredArgsConstructor
@Api("验证控制器")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final VerifyCodeService verifyCodeService;

    @ApiOperation("发送注册验证码")
    @PostMapping("/register/{email}")
    @PreAuthorize("isAnonymous()")
    public ApiResult<?> sendRegisterCode(@PathVariable("email") final String email, final HttpServletRequest request) {
        final Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            return ApiResult.bindException("已经有用户绑定了该邮箱");
        }
        verifyCodeService.generate(
            email,
            "验证您注册的邮箱",
            60L * 30L,
            request.getRemoteAddr(),
            RandomUtil.randomString(10)
        );
        return ApiResult.ok("验证码发送成功").build();
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> register(@Valid @JsonModel final RegisterUserView vo) {
        final Optional<VerifyCode> verifyCode = verifyCodeService.find("验证您注册的邮箱", vo.getCode());
        if (verifyCode.isEmpty() || !verifyCode.get().getEmail().equals(vo.getEmail())) {
            return ApiResult.bindException("验证码无效");
        }
        if (this.userRepository.findByUsernameOrEmail(vo.getUsername(), vo.getEmail()).isPresent()) {
            return ApiResult.bindException("用户名或邮箱已存在");
        }
        final User user = User.ofRegister(vo);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(this.roleRepository.findById("USER").get()));
        this.userRepository.save(user);
        return ApiResult.ok("注册成功").build();
    }

    @ApiOperation("Token 登录")
    @PostMapping("/api/login")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> apiLogin(@RequestParam("token") final String token, final HttpServletRequest request) {
        final Optional<Token> tokenOptional = this.tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            return ApiResult.unauthorized("Token 无效").build();
        }
        final User user = tokenOptional.get().getUser();
        List<Role> roles = user.getRoles().stream().filter(Role::getStatus).collect(Collectors.toList());
        final UserDetails details = new UserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            roles.stream().map(Role::getName).collect(Collectors.toList()),
            roles.stream().flatMap(role -> role.getPermissions().stream()).distinct().collect(Collectors.toList()),
            user.getStatus()
        );
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            details,
            null,
            details.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ApiResult.ok(user.toView(), "登录成功");
    }

    @ApiOperation("发送找回密码验证码")
    @PostMapping("/reset-password/{email}")
    @PreAuthorize("isAnonymous()")
    public ApiResult<?> sendResetPasswordCode(
        @PathVariable("email") final String email,
        final HttpServletRequest request
    ) {
        final Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            return ApiResult.bindException("指定邮箱用户不存在");
        }
        verifyCodeService.generate(email, "找回密码", 60L * 30L, request.getRemoteAddr(), UUID.randomUUID().toString());
        return ApiResult.ok("验证码发送成功").build();
    }

    @ApiOperation("找回密码")
    @PutMapping("/reset-password")
    @PreAuthorize("isAnonymous()")
    public ApiResult<?> resetPassword(@Valid @JsonModel final ResetPasswordView vo) {
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
        userRepository.save(user);
        return ApiResult.ok("重置密码成功").build();
    }
}
