/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import java.util.Optional;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.config.DefaultSecurityConfig.SecurityConfigAdapter;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.user.entity.User;
import me.ixk.hoshi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Spring Security 配置
 *
 * @author Otstar Lin
 * @date 2021/5/2 上午 10:48
 */
@Configuration
public class SecurityConfig extends SecurityConfigAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests().anyRequest().permitAll();
        http
            .formLogin()
            .successHandler(
                (request, response, authentication) -> {
                    final UserDetails details = (UserDetails) authentication.getPrincipal();
                    final Optional<User> user = this.userRepository.findById(details.getId());
                    if (user.isEmpty()) {
                        ApiResult.unauthorized("用户 ID 不存在").build().toResponse(response);
                    } else {
                        ApiResult.ok(user.get()).toResponse(response);
                    }
                }
            )
            .failureHandler(
                (request, response, exception) ->
                    ApiResult.unauthorized(exception.getMessage()).build().toResponse(response)
            );
        http
            .logout()
            .logoutSuccessHandler(
                (request, response, authentication) -> ApiResult.ok("注销成功").build().toResponse(response)
            );
    }
}
