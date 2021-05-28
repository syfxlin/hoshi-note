/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Otstar Lin
 * @date 2021/5/6 下午 2:33
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("me.ixk.hoshi.common")
public class HoshiCommonAutoConfiguration {}
