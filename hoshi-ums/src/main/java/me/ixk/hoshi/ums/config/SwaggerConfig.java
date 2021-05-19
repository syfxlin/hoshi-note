/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.security.repository.RolesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.HttpAuthenticationBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Api 文档配置
 *
 * @author Otstar Lin
 * @date 2021/5/4 下午 8:14
 */
@Configuration
@EnableOpenApi
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Environment environment;
    private final RolesRepository rolesRepository;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            .groupName("ums")
            .select()
            .apis(RequestHandlerSelectors.basePackage("me.ixk.hoshi.ums"))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo() {
        final Contact contact = new Contact(
            environment.getProperty("hoshi.author.name"),
            environment.getProperty("hoshi.author.url"),
            environment.getProperty("hoshi.author.email")
        );
        return new ApiInfo(
            environment.getProperty("hoshi.name"),
            environment.getProperty("description"),
            environment.getProperty("hoshi.version"),
            "urn:tos",
            contact,
            "Apache 2.0",
            "https://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>()
        );
    }

    private List<SecurityScheme> securitySchemes() {
        return StreamSupport
            .stream(rolesRepository.findAll().spliterator(), false)
            .map(
                role ->
                    new HttpAuthenticationBuilder()
                        .name(role.getName().toLowerCase())
                        .scheme("bearer")
                        .description(role.getDescription())
                        .build()
            )
            .collect(Collectors.toList());
    }
}
