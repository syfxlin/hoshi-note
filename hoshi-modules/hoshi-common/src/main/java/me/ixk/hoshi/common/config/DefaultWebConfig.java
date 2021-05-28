/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Otstar Lin
 * @date 2021/5/22 19:16
 */
@Configuration
@RequiredArgsConstructor
public class DefaultWebConfig implements WebMvcConfigurer {

    private final List<HandlerMethodArgumentResolver> argumentResolvers;
    private final List<HandlerMethodReturnValueHandler> returnValueHandlers;
    private final List<HandlerInterceptor> interceptors;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(this.argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(final List<HandlerMethodReturnValueHandler> handlers) {
        handlers.addAll(this.returnValueHandlers);
    }

    @Override
    public void addInterceptors(@NotNull final InterceptorRegistry registry) {
        this.interceptors.forEach(registry::addInterceptor);
    }
}
