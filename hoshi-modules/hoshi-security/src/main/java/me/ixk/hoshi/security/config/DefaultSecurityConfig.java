/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import lombok.RequiredArgsConstructor;
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
 * 默认安全配置
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:29
 */
@Configuration
public class DefaultSecurityConfig {

    /**
     * 使用 BCrypt 算法摘要密码
     *
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全配置适配器
     *
     * @return 适配器
     */
    @Bean
    @ConditionalOnMissingBean(SecurityConfigAdapter.class)
    public SecurityConfigAdapter securityConfigAdapter() {
        return new DefaultSecurityConfigAdapter();
    }

    /**
     * Spring Actuaor 安全配置
     * <p>
     * 只有是 Spring Actuator 的路由，才会进行检查
     * <p>
     * 安全检查使用的是 HttpBasic 验证，需要有 ACTUATOR 权限
     */
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

    @RequiredArgsConstructor
    public static class SecurityConfigAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            // 使用了 Session Token 的登录方式，可以关闭 CSRF 验证
            http.csrf().disable();
            // 允许跨域请求
            http.cors();
            // 异常处理
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
            // 关闭 Form 登录
            http.formLogin().authenticationDetailsSource(WebAuthenticationDetails::new).disable();
        }
    }
}
