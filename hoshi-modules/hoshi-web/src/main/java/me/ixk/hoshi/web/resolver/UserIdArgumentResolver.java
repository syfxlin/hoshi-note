/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.resolver;

import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.web.annotation.UserId;
import me.ixk.hoshi.web.util.Security;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 用户 ID 注入器
 * <p>
 * 标注了 {@link UserId} 注解的参数会自动注入当前登录用户的 ID
 *
 * @author Otstar Lin
 * @date 2021/5/28 22:56
 */
@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return (
            parameter.hasParameterAnnotation(UserId.class) && Long.class.isAssignableFrom(parameter.getParameterType())
        );
    }

    @Override
    public Object resolveArgument(
        @NotNull final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        @NotNull final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        return Security.id();
    }
}
