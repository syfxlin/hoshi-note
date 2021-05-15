/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Configuration
public class DefaultSecurityConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * TODO: 后期更改为通过数据库读取
     *
     * @return 用户授权
     */
    @Bean
    public UserDetailsService userDetailsService() {
        final Function<String, String> encode = passwordEncoder::encode;
        return new InMemoryUserDetailsManager(
            User
                .builder()
                .passwordEncoder(encode)
                .username("admin")
                .password("password")
                .roles(Roles.ADMIN.name(), Roles.ACTUATOR.name())
                .build(),
            User
                .builder()
                .passwordEncoder(encode)
                .username("hoshi-admin")
                .password("123456")
                .roles(Roles.ACTUATOR.name(), Roles.BOOT_ADMIN.name())
                .build()
        );
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                .requestMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeRequests()
                .anyRequest()
                .hasRole(Roles.ACTUATOR.name())
                .and()
                .httpBasic();
        }
    }
}
