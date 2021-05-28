/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.admin.config;

import me.ixk.hoshi.security.config.DefaultSecurityConfig.SecurityConfigAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 2:00
 */
@Configuration
public class SecurityConfig extends SecurityConfigAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().hasRole("BOOT_ADMIN");
        http.formLogin();
    }
}
