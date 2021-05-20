package me.ixk.hoshi.security.security;

import java.util.Optional;
import me.ixk.hoshi.user.entity.Users;
import me.ixk.hoshi.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 2:52
 */
@Component
public class UserDetailsManager implements UserDetailsService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<Users> user = this.usersRepository.findByUsernameAndStatusTrue(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("用户名不存在或已禁用");
        }
        return new me.ixk.hoshi.security.security.UserDetails(user.get());
    }
}
