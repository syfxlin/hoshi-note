/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.handler;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.json.DynamicFilterProvider;
import me.ixk.hoshi.common.json.DynamicFilterResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * Jackson 动态过滤器响应切面
 *
 * @author Otstar Lin
 * @date 2021/6/1 13:51
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class DynamicFilterResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    /**
     * 容器内配置的所有动态过滤器
     */
    private final List<DynamicFilterResolver<?>> resolvers;

    @Override
    protected void beforeBodyWriteInternal(
        @NotNull final MappingJacksonValue bodyContainer,
        @NotNull final MediaType contentType,
        @NotNull final MethodParameter returnType,
        @NotNull final ServerHttpRequest request,
        @NotNull final ServerHttpResponse response
    ) {
        // 找到第一个可用（不为 null）的 PropertyFilter，设置到 MappingJacksonValue
        this.resolvers.stream()
            .map(r -> r.resolve(returnType))
            .filter(Objects::nonNull)
            .findFirst()
            .map(DynamicFilterProvider::new)
            .ifPresent(bodyContainer::setFilters);
    }
}
