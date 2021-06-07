/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import me.ixk.hoshi.swagger.config.DefaultSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Api 文档配置
 *
 * @author Otstar Lin
 * @date 2021/5/4 下午 8:14
 */
@Configuration
public class SwaggerConfig extends DefaultSwaggerConfig {

    public SwaggerConfig(final Environment environment) {
        super(environment);
    }

    @Bean
    public Docket docket() {
        return this.buildDocket("ums", "me.ixk.hoshi.ums");
    }
}
