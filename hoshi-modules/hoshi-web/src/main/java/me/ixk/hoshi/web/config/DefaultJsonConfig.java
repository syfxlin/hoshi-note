/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.config;

import me.ixk.hoshi.web.json.DynamicFilterMixIn;
import me.ixk.hoshi.web.json.DynamicFilterProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认 Jackson 配置
 *
 * @author Otstar Lin
 * @date 2021/6/1 10:05
 */
@Configuration
public class DefaultJsonConfig {

    /**
     * 添加动态过滤器，同时配置 JsonView 默认包含
     *
     * @return {@link Jackson2ObjectMapperBuilderCustomizer}
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder ->
            builder
                .defaultViewInclusion(true)
                .mixIn(Object.class, DynamicFilterMixIn.class)
                .filters(new DynamicFilterProvider());
    }
}
