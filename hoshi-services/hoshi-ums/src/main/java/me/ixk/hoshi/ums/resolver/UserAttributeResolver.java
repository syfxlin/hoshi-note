/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.resolver;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiBindException;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.web.util.Security;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Otstar Lin
 * @date 2021/5/27 22:04
 */
@Component
@RequiredArgsConstructor
public class UserAttributeResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return (
            parameter.hasParameterAnnotation(Autowired.class) &&
            User.class.isAssignableFrom(parameter.getParameterType())
        );
    }

    @Override
    public Object resolveArgument(
        @NotNull final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        @NotNull final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        final Long userId = Security.id();
        if (userId == null) {
            return null;
        }
        final Optional<User> optional = this.userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new ApiBindException("用户登录状态无效（ID 失效，可能用户已被删除）");
        }
        return optional.get();
    }
}
