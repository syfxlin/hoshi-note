/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.config;

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
 * 默认 Web 配置
 *
 * @author Otstar Lin
 * @date 2021/5/22 19:16
 */
@Configuration
@RequiredArgsConstructor
public class DefaultWebConfig implements WebMvcConfigurer {

    private final List<HandlerMethodArgumentResolver> argumentResolvers;
    private final List<HandlerMethodReturnValueHandler> returnValueHandlers;
    private final List<HandlerInterceptor> interceptors;

    /**
     * 取出所有 {@link HandlerMethodArgumentResolver} Bean 并设置
     *
     * @param resolvers 处理器列表
     */
    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(this.argumentResolvers);
    }

    /**
     * 取出所有 {@link HandlerMethodReturnValueHandler} Bean 并设置
     *
     * @param handlers 处理器列表
     */
    @Override
    public void addReturnValueHandlers(final List<HandlerMethodReturnValueHandler> handlers) {
        handlers.addAll(this.returnValueHandlers);
    }

    /**
     * 取出所有 {@link HandlerInterceptor} Bean 并设置
     *
     * @param registry 处理器列表
     */
    @Override
    public void addInterceptors(@NotNull final InterceptorRegistry registry) {
        this.interceptors.forEach(registry::addInterceptor);
    }
}
