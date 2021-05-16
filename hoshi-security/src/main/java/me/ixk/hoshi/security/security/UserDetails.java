package me.ixk.hoshi.security.security;

import java.util.Arrays;
import me.ixk.hoshi.security.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 3:01
 */
public class UserDetails extends User {

    private static final long serialVersionUID = -7340031375619260648L;
    private static final String ANONYMOUS_USER = "anonymousUser";
    private final Users user;

    public UserDetails(final Users user) {
        super(
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.createAuthorityList(
                Arrays
                    .stream(StringUtils.tokenizeToStringArray(user.getRoles(), ","))
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .toArray(String[]::new)
            )
        );
        this.user = user;
    }

    public Users getUser() {
        return this.user;
    }

    public static Users currentUser() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        final Object details = authentication.getPrincipal();
        if (ANONYMOUS_USER.equals(details)) {
            return null;
        }
        return ((UserDetails) details).getUser();
    }
}