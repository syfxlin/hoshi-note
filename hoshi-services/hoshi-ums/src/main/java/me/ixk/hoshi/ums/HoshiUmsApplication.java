/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums;

import me.ixk.hoshi.mail.HoshiMailApiAutoConfiguration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * 用户中心
 *
 * @author Otstar Lin
 * @date 2021/11/14 13:33
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@EnableRedisRepositories(
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
    basePackageClasses = HoshiMailApiAutoConfiguration.class
)
public class HoshiUmsApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HoshiUmsApplication.class, args);
    }
}
