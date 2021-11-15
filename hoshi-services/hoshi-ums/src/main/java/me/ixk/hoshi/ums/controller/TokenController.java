/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Token 控制器
 *
 * @author Otstar Lin
 * @date 2021/11/4 20:47
 */
@RestController
@RequestMapping("/tokens")
@Api("Token 控制器")
@RequiredArgsConstructor
public class TokenController {

    private final TokenRepository tokenRepository;

    @ApiOperation("获取所有 Token")
    @GetMapping("")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<List<Token>> list(@Autowired final User user) {
        return ApiResult.ok(this.tokenRepository.findByUserId(user.getId()), "获取所有 Token 成功");
    }

    @ApiOperation("新增 Token")
    @PostMapping("/{name}")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<Object> add(@Autowired final User user, @PathVariable("name") final String name) {
        if (this.tokenRepository.findByNameAndUserId(name, user.getId()).isPresent()) {
            return ApiResult.bindException("Token 已存在");
        }
        return ApiResult.ok(
            this.tokenRepository.save(
                    Token.builder().name(name).token(UUID.randomUUID().toString()).user(user).build()
                ),
            "添加 Token 成功"
        );
    }

    @ApiOperation("撤销 Token")
    @DeleteMapping("/{token}")
    @PreAuthorize("hasAuthority('ME')")
    public ApiResult<Object> revoke(@Autowired final User user, @PathVariable("token") final String token) {
        final Optional<Token> optionalToken = this.tokenRepository.findByTokenAndUserId(token, user.getId());
        if (optionalToken.isEmpty()) {
            return ApiResult.bindException("Token 未找到");
        }
        this.tokenRepository.delete(optionalToken.get());
        return ApiResult.ok("撤销成功").build();
    }
}
