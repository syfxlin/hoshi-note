/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 自动配置
 *
 * @author Otstar Lin
 * @date 2021/5/6 下午 2:33
 */
@Configuration
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@ComponentScan
public class HoshiCommonAutoConfiguration {}
