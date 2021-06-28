/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.RoleRepository;
import me.ixk.hoshi.ums.repository.TokenRepository;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.view.request.RegisterUserView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
