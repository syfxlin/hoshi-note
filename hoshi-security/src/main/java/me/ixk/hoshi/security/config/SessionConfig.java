/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * Session 配置
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 9:48
 */
@Configuration
public class SessionConfig {

    @Bean
    public HttpSessionIdResolver sessionIdResolver() {
        return new CompositeSessionIdResolver();
    }
}
