/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.security;

import java.util.List;
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
        final Optional<User> optional = this.userRepository.findByUsernameOrEmail(username, username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        final User user = optional.get();
        List<Role> roles = user.getRoles().stream().filter(Role::getStatus).collect(Collectors.toList());
        return new me.ixk.hoshi.security.security.UserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            roles.stream().map(Role::getName).collect(Collectors.toList()),
            roles.stream().flatMap(role -> role.getPermissions().stream()).distinct().collect(Collectors.toList()),
            user.getStatus()
        );
    }
}
