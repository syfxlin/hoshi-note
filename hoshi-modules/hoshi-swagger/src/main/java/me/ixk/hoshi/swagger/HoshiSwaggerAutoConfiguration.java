/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.swagger;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 自动配置
 *
 * @author Otstar Lin
 * @date 2021/5/20 20:11
 */
@Configuration
@ComponentScan
@EnableOpenApi
public class HoshiSwaggerAutoConfiguration {}
