/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 自动配置
 *
 * @author Otstar Lin
 * @date 2021/5/3 下午 10:12
 */
@Configuration
@ComponentScan("me.ixk.hoshi.security")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HoshiSecurityAutoConfiguration {}