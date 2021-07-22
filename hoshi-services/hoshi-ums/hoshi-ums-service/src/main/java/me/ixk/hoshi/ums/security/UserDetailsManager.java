/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.security;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 2:52
 */
@RequiredArgsConstructor
public class UserDetailsManager implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<User> optional = this.userRepository.findByUsername(username);
        if (optional.isEmpty()) {
            optional = this.userRepository.findByEmail(username);
        }
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        final User user = optional.get();
        return new me.ixk.hoshi.security.security.UserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream().filter(Role::getStatus).map(Role::getName).collect(Collectors.toList()),
            user.getStatus()
        );
    }
}
