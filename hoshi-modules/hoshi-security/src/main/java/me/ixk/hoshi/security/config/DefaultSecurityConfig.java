/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import me.ixk.hoshi.common.result.ApiMessage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.security.WebAuthenticationDetails;
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
        return new DefaultSecurityConfigAdapter();
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
                .hasRole("ACTUATOR")
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
                .accessDeniedHandler(
                    (request, response, e) ->
                        ApiResult.status(ApiMessage.FORBIDDEN).message(e.getMessage()).build().toResponse(response)
                )
                .authenticationEntryPoint(
                    (request, response, e) -> ApiResult.unauthorized(e.getMessage()).build().toResponse(response)
                );
        }
    }

    protected static class DefaultSecurityConfigAdapter extends SecurityConfigAdapter {

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            super.configure(http);
            http.formLogin().authenticationDetailsSource(WebAuthenticationDetails::new).disable();
        }
    }
}
