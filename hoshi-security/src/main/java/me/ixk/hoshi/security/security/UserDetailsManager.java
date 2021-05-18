package me.ixk.hoshi.security.security;

import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.service.RolesService;
import me.ixk.hoshi.security.service.UsersService;
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
    private UsersService usersService;

    @Autowired
    private RolesService rolesService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Users user =
            this.usersService.query().eq(Users.USERNAME, username).eq(Users.STATUS, Status.ENABLE.ordinal()).one();
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在或已禁用");
        }
        return new me.ixk.hoshi.security.security.UserDetails(user, rolesService.queriesByUser(user));
    }
}
