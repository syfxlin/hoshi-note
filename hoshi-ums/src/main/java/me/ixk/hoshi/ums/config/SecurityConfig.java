/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.PrintWriter;
import me.ixk.hoshi.common.result.Result;
import me.ixk.hoshi.common.util.Json;
import me.ixk.hoshi.security.config.DefaultSecurityConfig.SecurityConfigAdapter;
import me.ixk.hoshi.security.security.UserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Spring Security 配置
 *
 * @author Otstar Lin
 * @date 2021/5/2 上午 10:48
 */
@Configuration
public class SecurityConfig extends SecurityConfigAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests().anyRequest().permitAll();
        http
            .formLogin()
            .successHandler(
                (request, response, authentication) -> {
                    final UserDetails principal = (UserDetails) authentication.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    final PrintWriter writer = response.getWriter();
                    final ObjectNode result = Json.convertToObjectNode(principal.getUser());
                    result.put("token", RequestContextHolder.currentRequestAttributes().getSessionId());
                    writer.write(result.toString());
                }
            )
            .failureHandler(
                (request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    final PrintWriter writer = response.getWriter();
                    writer.write(Json.stringify(Result.error(4001, exception.getMessage())));
                }
            );
        http
            .exceptionHandling()
            .authenticationEntryPoint(
                (request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    final PrintWriter writer = response.getWriter();
                    writer.write(Json.stringify(Result.error(4001, "尚未登录，请先登录")));
                }
            );
        http
            .logout()
            .logoutSuccessHandler(
                (request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    final PrintWriter writer = response.getWriter();
                    writer.write(Json.stringify(Result.data("注销成功")));
                }
            );
    }
}
