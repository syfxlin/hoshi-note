/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.session.config;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.session.entity.TokenSession;
import me.ixk.hoshi.session.repository.TokenSessionRepository;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:49
 */
@Component
@RequiredArgsConstructor
public class CompositeSessionIdResolver implements HttpSessionIdResolver {

    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String SESSION_MARK = "session.";
    public static final String TOKEN_MARK = "token.";

    private final TokenSessionRepository tokenSessionRepository;

    @Override
    public List<String> resolveSessionIds(final HttpServletRequest request) {
        final String token = getToken(request);
        if (token == null) {
            return Collections.emptyList();
        }
        if (token.startsWith(TOKEN_MARK)) {
            final Optional<TokenSession> session = this.tokenSessionRepository.findById(token.replace(TOKEN_MARK, ""));
            if (session.isEmpty()) {
                return Collections.emptyList();
            }
            return Collections.singletonList(session.get().getSession());
        } else if (token.startsWith(SESSION_MARK)) {
            return Collections.singletonList(token.replace(SESSION_MARK, ""));
        }
        return Collections.singletonList(token);
    }

    @Override
    public void setSessionId(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String sessionId
    ) {
        response.setHeader(X_AUTH_TOKEN, SESSION_MARK + sessionId);
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
