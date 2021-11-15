/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Otstar Lin
 * @date 2021/11/13 19:18
 */
@SpringBootApplication
@EnableEurekaServer
public class HoshiRegistryApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiRegistryApplication.class, args);
    }
}
