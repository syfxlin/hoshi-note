/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Otstar Lin
 * @date 2021/6/4 14:22
 */
public class TokenAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -8340379889465902411L;
    private final Object principal;
    private final Object token;

    public TokenAuthentication(final String token) {
        super(null);
        this.token = token;
        this.principal = null;
    }

    public TokenAuthentication(
        final Object principal,
        final String token,
        final Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
