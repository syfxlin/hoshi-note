package me.ixk.hoshi.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

/**
 * @author Otstar Lin
 * @date 2022/2/5 上午 12:02
 */
@Configuration
public class SecurityConfig {

    @Value("${management.security.username}")
    private String username;

    @Value("${management.security.password}")
    private String password;

    /**
     * 使用 BCrypt 算法摘要密码
     *
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Actuaor 安全配置
     * <p>
     * 只有是 Spring Actuator 的路由，才会进行检查
     * <p>
     * 安全检查使用的是 HttpBasic 验证，需要有 ACTUATOR 权限
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain actuatorSecurityChain(ServerHttpSecurity http) {
        return http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
            .authorizeExchange()
            .anyExchange()
            .hasAuthority("ACTUATOR")
            .and()
            .httpBasic()
            .and()
            .build();
    }

    @Bean
    public SecurityWebFilterChain defaultSecurityChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
            .anyExchange()
            .permitAll()
            .and()
            .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        UserDetails actuator = User.withUsername(username)
            .password(password)
            .authorities("ACTUATOR")
            .build();
        return new MapReactiveUserDetailsService(actuator);
    }
}
