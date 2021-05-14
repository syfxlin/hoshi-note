/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 安全配置
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:29
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(Roles.ACTUATOR_ADMIN.name());
        http.authorizeRequests().anyRequest().permitAll();
    }

    /**
     * TODO: 后期更改为通过数据库读取
     *
     * @return 用户授权
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        final Function<String, String> encode = passwordEncoder::encode;
        return new InMemoryUserDetailsManager(
            User
                .builder()
                .passwordEncoder(encode)
                .username("admin")
                .password("password")
                .roles(Roles.ADMIN.name(), Roles.ACTUATOR_ADMIN.name())
                .build(),
            User
                .builder()
                .passwordEncoder(encode)
                .username("hoshi-admin")
                .password("123456")
                .roles(Roles.ACTUATOR_ADMIN.name())
                .build()
        );
    }
}
