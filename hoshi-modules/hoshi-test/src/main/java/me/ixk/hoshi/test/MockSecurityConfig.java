/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import me.ixk.hoshi.security.security.UserDetails;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Otstar Lin
 * @date 2021/5/26 22:05
 */
@TestConfiguration
public class MockSecurityConfig {

    private final List<UserDetails> users = Collections.singletonList(
        new UserDetails("1234567890", "admin", "password", Arrays.asList("ADMIN", "USER"), true)
    );

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
            final Optional<UserDetails> optional =
                this.users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
            if (optional.isEmpty()) {
                throw new UsernameNotFoundException("用户名未找到");
            }
            return optional.get();
        };
    }
}
