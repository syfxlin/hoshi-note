/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.client.config;

import me.ixk.hoshi.client.util.Request;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
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
        return Request.addToken(new RestTemplate());
    }
}
