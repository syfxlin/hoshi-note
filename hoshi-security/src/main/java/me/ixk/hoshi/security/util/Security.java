package me.ixk.hoshi.security.util;

import me.ixk.hoshi.common.exception.UnsupportedInstantiationException;
import me.ixk.hoshi.security.security.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Otstar Lin
 * @date 2021/5/22 15:41
 */
public final class Security {

    public static final String ANONYMOUS_USER = "anonymousUser";

    private Security() throws UnsupportedInstantiationException {
        throw new UnsupportedInstantiationException(Security.class);
    }

    public static Long id() {
        final UserDetails details = principal();
        if (details == null) {
            return null;
        }
        return details.getId();
    }

    public static UserDetails principal() {
        final Object principal = authentication().getPrincipal();
        if (ANONYMOUS_USER.equals(principal)) {
            return null;
        }
        return (UserDetails) principal;
    }

    public static Authentication authentication() {
        return context().getAuthentication();
    }

    public static SecurityContext context() {
        return SecurityContextHolder.getContext();
    }
}
