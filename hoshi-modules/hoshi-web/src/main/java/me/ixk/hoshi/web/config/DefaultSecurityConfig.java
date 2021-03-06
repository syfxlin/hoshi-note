/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.config;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiMessage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.web.details.WebAuthenticationDetails;
import me.ixk.hoshi.web.result.ApiResultUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .accessDeniedHandler((request, response, e) ->
                    ApiResultUtil.toResponse(
                        ApiResult.status(ApiMessage.FORBIDDEN).message(e.getMessage()).build(),
                        response
                    )
                )
                .authenticationEntryPoint((request, response, e) ->
                    ApiResultUtil.toResponse(ApiResult.unauthorized(e.getMessage()).build(), response)
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
