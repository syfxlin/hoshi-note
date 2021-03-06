/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Token 控制器
 *
 * @author Otstar Lin
 * @date 2021/11/4 20:47
 */
@RestController
@RequestMapping("/tokens")
@Tag(name = "Token 控制器")
@RequiredArgsConstructor
public class TokenController {

    private final TokenRepository tokenRepository;

    @Operation(summary = "获取所有 Token")
    @GetMapping("")
    @PreAuthorize("hasAuthority('TOKEN')")
    public ApiResult<?> list(@Autowired final User user) {
        return ApiResult.ok(
            this.tokenRepository.findByUserId(user.getId()).stream().map(Token::toView).collect(Collectors.toList()),
            "获取所有 Token 成功"
        );
    }

    @Operation(summary = "新增 Token")
    @PostMapping("/{name}")
    @PreAuthorize("hasAuthority('TOKEN')")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public ApiResult<?> add(@Autowired final User user, @PathVariable("name") final String name) {
        if (this.tokenRepository.findByNameAndUserId(name, user.getId()).isPresent()) {
            return ApiResult.bindException("Token 已存在");
        }
        return ApiResult.ok(
            this.tokenRepository.save(Token.builder().name(name).token(UUID.randomUUID().toString()).user(user).build())
                .toView(),
            "添加 Token 成功"
        );
    }

    @Operation(summary = "撤销 Token")
    @DeleteMapping("/{token}")
    @PreAuthorize("hasAuthority('TOKEN')")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public ApiResult<?> revoke(@Autowired final User user, @PathVariable("token") final String token) {
        final Optional<Token> optionalToken = this.tokenRepository.findByTokenAndUserId(token, user.getId());
        if (optionalToken.isEmpty()) {
            return ApiResult.bindException("Token 未找到");
        }
        this.tokenRepository.delete(optionalToken.get());
        return ApiResult.ok("撤销成功").build();
    }
}
