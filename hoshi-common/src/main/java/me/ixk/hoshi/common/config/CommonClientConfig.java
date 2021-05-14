/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.config;

import me.ixk.hoshi.common.util.RequestUtil;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Otstar Lin
 * @date 2021/5/6 下午 2:32
 */
@Configuration
@EnableFeignClients(defaultConfiguration = { CommonFeignConfig.class }, basePackages = { "me.ixk.hoshi" })
@EnableDiscoveryClient
public class CommonClientConfig {

    /**
     * 负载均衡 RestTemplate
     *
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return RequestUtil.wrapperHeaders(new RestTemplate());
    }
}
