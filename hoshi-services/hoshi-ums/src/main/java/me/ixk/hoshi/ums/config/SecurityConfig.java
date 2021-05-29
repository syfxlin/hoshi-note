/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.config.DefaultSecurityConfig.SecurityConfigAdapter;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.security.security.WebAuthenticationDetails;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.security.UserDetailsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Spring Security 配置
 *
 * @author Otstar Lin
 * @date 2021/5/2 上午 10:48
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends SecurityConfigAdapter {

    private final UserRepository userRepository;

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
            )
            .authenticationDetailsSource(WebAuthenticationDetails::new);
        http
            .logout()
            .logoutSuccessHandler(
                (request, response, authentication) -> ApiResult.ok("注销成功").build().toResponse(response)
            );
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsManager(this.userRepository);
    }
}
