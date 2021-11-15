/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.config;

import me.ixk.hoshi.api.util.Request;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 默认客户端配置
 *
 * @author Otstar Lin
 * @date 2021/5/6 下午 2:32
 */
@Configuration
public class DefaultClientConfig {

    /**
     * 负载均衡 RestTemplate
     *
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        // 默认添加认证 Token，从 HttpServletRequest 读取
        return Request.addToken(new RestTemplate());
    }
}
