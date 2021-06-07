/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.security;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.security.security.UserDetails;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.Token;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.TokenRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Otstar Lin
 * @date 2021/6/4 14:41
 */
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenRepository tokenRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        }
        final String tokenString = authentication.getCredentials().toString();
        if (tokenString == null || tokenString.isBlank()) {
            throw new BadCredentialsException("Token 不能为空值 [" + tokenString + "]");
        }
        final Optional<Token> token = this.tokenRepository.findById(tokenString);
        if (token.isEmpty()) {
            throw new BadCredentialsException("Token 不存在 [" + tokenString + "]");
        }
        final User user = token.get().getUser();
        final UserDetails details = new UserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream().filter(Role::getStatus).map(Role::getName).collect(Collectors.toList()),
            user.getStatus()
        );
        final TokenAuthentication tokenAuthentication = new TokenAuthentication(
            details,
            tokenString,
            details.getAuthorities()
        );
        tokenAuthentication.setDetails(authentication.getDetails());
        return tokenAuthentication;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return TokenAuthentication.class.isAssignableFrom(authentication);
    }
}
