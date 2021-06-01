/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/6/1 10:05
 */
@Configuration
public class DefaultJsonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.defaultViewInclusion(true);
    }
}
