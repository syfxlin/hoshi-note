/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.swagger.config;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Otstar Lin
 * @date 2021/5/20 20:09
 */
@RequiredArgsConstructor
public class DefaultSwaggerConfig {

    private final Environment environment;

    protected Docket buildDocket(final String groupName, final String basePackage) {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(this.apiInfo())
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors.basePackage(basePackage))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        final Contact contact = new Contact(
            this.environment.getProperty("hoshi.author.name"),
            this.environment.getProperty("hoshi.author.url"),
            this.environment.getProperty("hoshi.author.email")
        );
        return new ApiInfo(
            this.environment.getProperty("hoshi.name"),
            this.environment.getProperty("description"),
            this.environment.getProperty("hoshi.version"),
            "urn:tos",
            contact,
            "Apache 2.0",
            "https://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>()
        );
    }
}
