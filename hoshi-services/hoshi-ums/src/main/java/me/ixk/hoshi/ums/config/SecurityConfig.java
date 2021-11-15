/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.security.AuthenticationHandler;
import me.ixk.hoshi.ums.security.UserDetailsManager;
import me.ixk.hoshi.web.config.DefaultSecurityConfig.SecurityConfigAdapter;
import me.ixk.hoshi.web.details.WebAuthenticationDetails;
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
    private final AuthenticationHandler authenticationHandler;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests().anyRequest().permitAll();
        http
            .formLogin()
            .successHandler(this.authenticationHandler)
            .failureHandler(this.authenticationHandler)
            .authenticationDetailsSource(WebAuthenticationDetails::new);
        http.logout().logoutSuccessHandler(this.authenticationHandler);
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsManager(this.userRepository);
    }
}
