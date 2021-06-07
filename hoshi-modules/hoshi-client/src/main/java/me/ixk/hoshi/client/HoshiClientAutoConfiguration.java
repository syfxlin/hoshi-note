/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.client;

import me.ixk.hoshi.client.config.DefaultFeignConfig;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *
 * @author Otstar Lin
 * @date 2021/5/20 20:42
 */
@Configuration
@ComponentScan
@EnableFeignClients(defaultConfiguration = { DefaultFeignConfig.class }, basePackages = { "me.ixk.hoshi" })
@EnableDiscoveryClient
public class HoshiClientAutoConfiguration {}
