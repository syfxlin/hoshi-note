package me.ixk.hoshi.admin.config;

import me.ixk.hoshi.security.security.Roles;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 2:00
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().hasRole(Roles.BOOT_ADMIN.name());
        http.formLogin();
    }
}
