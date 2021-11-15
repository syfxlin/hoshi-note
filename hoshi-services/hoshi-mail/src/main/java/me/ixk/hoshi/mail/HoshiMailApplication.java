/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mail;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Otstar Lin
 * @date 2021/7/17 16:18
 */
@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
public class HoshiMailApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiMailApplication.class, args);
    }
}
