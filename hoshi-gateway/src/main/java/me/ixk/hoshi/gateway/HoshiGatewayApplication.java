/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * 服务网关
 *
 * @author Otstar Lin
 * @date 2021/6/24 19:39
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableWebFluxSecurity
public class HoshiGatewayApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiGatewayApplication.class, args);
    }
}
