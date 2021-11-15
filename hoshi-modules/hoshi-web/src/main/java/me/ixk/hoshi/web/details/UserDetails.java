/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.details;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * 用户描述信息
 * <p>
 * 继承自 {@link org.springframework.security.core.userdetails.User} 同时增加保存了用户 ID
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 3:01
 */
public class UserDetails extends org.springframework.security.core.userdetails.User {

    private final Long userId;

    public UserDetails(
        final Long userId,
        final String username,
        final String password,
        final List<String> roles,
        final List<String> permissions,
        final boolean status
    ) {
        super(
            username,
            password,
            status,
            true,
            true,
            true,
            AuthorityUtils.createAuthorityList(
                Stream
                    .concat(roles.stream().map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r), permissions.stream())
                    .toArray(String[]::new)
            )
        );
        this.userId = userId;
    }

    public Long getId() {
        return this.userId;
    }
}
