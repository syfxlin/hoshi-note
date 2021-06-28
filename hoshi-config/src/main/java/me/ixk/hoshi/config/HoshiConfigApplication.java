/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 *
 * @author Otstar Lin
 * @date 2021/6/24 19:39
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class HoshiConfigApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiConfigApplication.class, args);
    }
}
