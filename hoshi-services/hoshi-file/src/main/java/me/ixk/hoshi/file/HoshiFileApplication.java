/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Otstar Lin
 * @date 2021/5/23 9:04
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HoshiFileApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiFileApplication.class, args);
    }
}
