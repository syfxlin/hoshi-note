package me.ixk.hoshi.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/5/14 下午 9:07
 */
@Configuration
public class OnMissSecurityConfig {

    @Bean
    @ConditionalOnMissingBean(SecurityConfig.class)
    public SecurityConfig securityConfig() {
        return new SecurityConfig();
    }
}
