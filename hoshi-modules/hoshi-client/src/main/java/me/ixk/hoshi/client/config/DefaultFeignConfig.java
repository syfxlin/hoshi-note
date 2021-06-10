/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.client.config;

import feign.RequestInterceptor;
import me.ixk.hoshi.client.util.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认 Feign 配置
 *
 * @author Otstar Lin
 * @date 2021/5/9 下午 2:52
 */
@Configuration
public class DefaultFeignConfig {

    /**
     * Feign 默认请求过滤器
     *
     * @return 请求过滤器
     */
    @Bean
    public RequestInterceptor sessionRequestInterceptor() {
        // 默认添加认证 Token，从 HttpServletRequest 读取
        return Request::addToken;
    }
}
