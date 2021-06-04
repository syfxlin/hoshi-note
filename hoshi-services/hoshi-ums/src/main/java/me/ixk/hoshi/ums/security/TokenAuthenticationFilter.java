/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.ixk.hoshi.session.config.CompositeSessionIdResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Otstar Lin
 * @date 2021/6/4 14:34
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NotNull final HttpServletRequest request,
        @NotNull final HttpServletResponse response,
        @NotNull final FilterChain chain
    ) throws ServletException, IOException {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            final String token = CompositeSessionIdResolver.getToken(request);
            if (token != null && token.startsWith(CompositeSessionIdResolver.TOKEN_MARK)) {
                final TokenAuthentication auth = new TokenAuthentication(
                    token.replace(CompositeSessionIdResolver.TOKEN_MARK, "")
                );
                auth.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
