/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.security;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.security.security.WebAuthenticationDetails;
import me.ixk.hoshi.session.config.CompositeSessionIdResolver;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.TokenRepository;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;

/**
 * @author Otstar Lin
 * @date 2021/6/4 12:37
 */
// @Component
@RequiredArgsConstructor
public class TokenAutoLogin implements RememberMeServices {

    private final TokenRepository tokenRepository;

    @Override
    public Authentication autoLogin(final HttpServletRequest request, final HttpServletResponse response) {
        final String token = CompositeSessionIdResolver.getToken(request);
        if (token == null) {
            return null;
        }
        if (!token.startsWith(CompositeSessionIdResolver.TOKEN_MARK)) {
            return null;
        }
        final Optional<Token> optional =
            this.tokenRepository.findById(token.replace(CompositeSessionIdResolver.TOKEN_MARK, ""));
        if (optional.isEmpty()) {
            return null;
        }
        final User user = optional.get().getUser();
        final UserDetails details = new UserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream().filter(Role::getStatus).map(Role::getName).collect(Collectors.toList()),
            user.getStatus()
        );
        final RememberMeAuthenticationToken authenticationToken = new RememberMeAuthenticationToken(
            token,
            details,
            details.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        return authenticationToken;
    }

    @Override
    public void loginFail(final HttpServletRequest request, final HttpServletResponse response) {}

    @Override
    public void loginSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication successfulAuthentication
    ) {}
}
