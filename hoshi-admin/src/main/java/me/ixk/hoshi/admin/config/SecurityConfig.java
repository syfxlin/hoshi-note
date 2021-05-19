package me.ixk.hoshi.admin.config;

import me.ixk.hoshi.security.config.DefaultSecurityConfig.SecurityConfigAdapter;
import me.ixk.hoshi.security.security.RoleNames;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 2:00
 */
@Configuration
public class SecurityConfig extends SecurityConfigAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().hasRole(RoleNames.BOOT_ADMIN.name());
        http.formLogin();
    }
}
