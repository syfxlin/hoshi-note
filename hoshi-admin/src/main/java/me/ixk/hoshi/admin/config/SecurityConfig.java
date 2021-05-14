package me.ixk.hoshi.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 安全配置
 *
 * @author Otstar Lin
 * @date 2021/5/14 下午 9:01
 */
@Configuration
public class SecurityConfig extends me.ixk.hoshi.security.config.SecurityConfig {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.formLogin();
    }
}
