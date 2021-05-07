/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Spring Security 配置
 *
 * @author Otstar Lin
 * @date 2021/5/2 上午 10:48
 */
@Configuration
public class SecurityConfig extends me.ixk.hoshi.security.config.SecurityConfig {

    public static final String[] ROLES = new String[] { "ADMIN", "USER" };

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.formLogin();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        final UserDetails userDetails = User
            .builder()
            .passwordEncoder(passwordEncoder()::encode)
            .username("admin")
            .password("password")
            .roles(ROLES)
            .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}
