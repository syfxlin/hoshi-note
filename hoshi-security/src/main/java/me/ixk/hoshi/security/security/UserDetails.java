package me.ixk.hoshi.security.security;

import me.ixk.hoshi.db.entity.Role;
import me.ixk.hoshi.db.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 3:01
 */
public class UserDetails extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -7340031375619260648L;
    private final Long userId;

    public UserDetails(final User user) {
        super(
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.createAuthorityList(
                user
                    .getRoles()
                    .stream()
                    .filter(Role::getStatus)
                    .map(Role::getName)
                    .map(r -> "ROLE_" + r)
                    .toArray(String[]::new)
            )
        );
        this.userId = user.getId();
    }

    public Long getId() {
        return this.userId;
    }
}
