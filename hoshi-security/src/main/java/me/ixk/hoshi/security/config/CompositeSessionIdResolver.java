/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * 支持 Cookie 和 Header 两种方式的 Session ID 解析器
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:49
 */
public class CompositeSessionIdResolver implements HttpSessionIdResolver {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final HeaderHttpSessionIdResolver headerResolver = new HeaderHttpSessionIdResolver(X_AUTH_TOKEN);

    @Override
    public List<String> resolveSessionIds(final HttpServletRequest request) {
        final List<String> ids = new ArrayList<>();
        final String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null && authorization.startsWith(BEARER)) {
            ids.add(authorization.replace(BEARER, ""));
        }
        ids.addAll(headerResolver.resolveSessionIds(request));
        return ids;
    }

    @Override
    public void setSessionId(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String sessionId
    ) {
        headerResolver.setSessionId(request, response, sessionId);
    }

    @Override
    public void expireSession(final HttpServletRequest request, final HttpServletResponse response) {
        response.setHeader(AUTHORIZATION, "");
        headerResolver.expireSession(request, response);
    }
}
