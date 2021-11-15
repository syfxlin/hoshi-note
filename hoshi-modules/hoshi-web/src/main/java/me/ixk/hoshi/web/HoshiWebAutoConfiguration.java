/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 自动配置
 *
 * @author Otstar Lin
 * @date 2021/11/13 21:42
 */
@Configuration
@ComponentScan
@EnableRedisHttpSession
@EnableAspectJAutoProxy
public class HoshiWebAutoConfiguration {}
