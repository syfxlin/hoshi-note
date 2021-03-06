/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.resolver;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * Token 解析器
 * <p>
 * 请求通过 <code>Authorization</code> 请求头传入 Token
 * <p>
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:49
 */
@Configuration
public class CompositeSessionIdResolver implements HttpSessionIdResolver {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Override
    public List<String> resolveSessionIds(final HttpServletRequest request) {
        final String token = getToken(request);
        if (token == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(token);
    }

    @Override
    public void setSessionId(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String sessionId
    ) {
        response.setHeader(X_AUTH_TOKEN, sessionId);
    }

    @Override
    public void expireSession(final HttpServletRequest request, final HttpServletResponse response) {
        response.setHeader(AUTHORIZATION, "");
        response.setHeader(X_AUTH_TOKEN, "");
    }

    public static String getToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER)) {
            return null;
        }
        return authorization.replace(BEARER, "");
    }
}
