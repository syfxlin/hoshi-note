/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Spring Security 配置
 *
 * @author Otstar Lin
 * @date 2021/5/2 上午 10:48
 */
@Configuration
public class SecurityConfig extends me.ixk.hoshi.security.config.SecurityConfig {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.formLogin();
    }
}
