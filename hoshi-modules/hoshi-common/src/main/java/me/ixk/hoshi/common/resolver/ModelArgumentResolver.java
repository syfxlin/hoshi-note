/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.resolver;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import me.ixk.hoshi.common.annotation.JsonModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

/**
 * @author Otstar Lin
 * @date 2021/5/26 15:59
 */
public class ModelArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {

    private final ApplicationContext applicationContext;

    public ModelArgumentResolver(
        final List<HttpMessageConverter<?>> converters,
        final List<Object> requestResponseBodyAdvice,
        final ApplicationContext applicationContext
    ) {
        super(converters, requestResponseBodyAdvice);
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean supportsParameter(@NotNull final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonModel.class);
    }

    @Override
    public Object resolveArgument(
        @NotNull MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        @NotNull final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) throws Exception {
        parameter = parameter.nestedIfOptional();
        final Object arg =
            this.readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
        final String name = Conventions.getVariableNameForParameter(parameter);
        if (binderFactory != null) {
            final WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
            if (arg != null) {
                final JsonModel jsonModel = parameter.getParameterAnnotation(JsonModel.class);
                if (jsonModel != null && !jsonModel.skipBind()) {
                    final ServletRequest nativeRequest = webRequest.getNativeRequest(ServletRequest.class);
                    if (nativeRequest != null) {
                        ((ServletRequestDataBinder) binder).bind(nativeRequest);
                    }
                }
                this.validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }
            if (mavContainer != null) {
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            }
        }

        return this.adaptArgumentIfNecessary(arg, parameter);
    }

    @Override
    protected <T> Object readWithMessageConverters(
        final NativeWebRequest webRequest,
        @NotNull final MethodParameter parameter,
        @NotNull final Type paramType
    ) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        final ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        final Object arg = this.readWithMessageConverters(inputMessage, parameter, paramType);
        if (arg == null && this.checkRequired(parameter)) {
            throw new HttpMessageNotReadableException(
                "Required request body is missing: " + parameter.getExecutable().toGenericString(),
                inputMessage
            );
        }
        return arg;
    }

    protected boolean checkRequired(final MethodParameter parameter) {
        final JsonModel jsonModel = parameter.getParameterAnnotation(JsonModel.class);
        return (jsonModel != null && jsonModel.required() && !parameter.isOptional());
    }

    @Override
    protected void validateIfApplicable(@NotNull final WebDataBinder binder, final MethodParameter parameter) {
        final Annotation[] annotations = parameter.getParameterAnnotations();
        for (final Annotation ann : annotations) {
            final Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                final Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                final Object[] validationHints =
                    (hints instanceof Object[] ? (Object[]) hints : new Object[] { hints });
                binder.validate(validationHints);
                break;
            }
        }
    }

    @Override
    protected Object adaptArgumentIfNecessary(@Nullable final Object arg, final MethodParameter parameter) {
        if (parameter.getParameterType() == Optional.class) {
            if (
                arg == null ||
                (arg instanceof Collection && ((Collection<?>) arg).isEmpty()) ||
                (arg instanceof Object[] && ((Object[]) arg).length == 0)
            ) {
                return Optional.empty();
            } else {
                return Optional.of(arg);
            }
        }
        return arg;
    }

    @Override
    protected boolean isBindExceptionRequired(@NotNull final WebDataBinder binder, final MethodParameter parameter) {
        final int i = parameter.getParameterIndex();
        final Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        final boolean hasBindingResult =
            (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }
}
