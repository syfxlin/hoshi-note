/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import java.io.PrintWriter;
import me.ixk.hoshi.common.result.Result;
import me.ixk.hoshi.common.util.Json;
import me.ixk.hoshi.security.security.Roles;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:29
 */
@Configuration
public class DefaultSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityConfigAdapter.class)
    public SecurityConfigAdapter securityConfigAdapter() {
        return new SecurityConfigAdapter();
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

    public static class SecurityConfigAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.cors();
            http
                .exceptionHandling()
                .authenticationEntryPoint(
                    (request, response, authException) -> {
                        response.setContentType("application/json;charset=utf-8");
                        final PrintWriter writer = response.getWriter();
                        writer.write(Json.stringify(Result.error(4001, "尚未登录，请先登录")));
                    }
                );
        }
    }
}