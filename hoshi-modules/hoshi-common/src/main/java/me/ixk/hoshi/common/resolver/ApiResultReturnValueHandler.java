/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.resolver;

import java.util.List;
import me.ixk.hoshi.common.annotation.ApiResultBody;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.ApiResult.HeadersBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

public class ApiResultReturnValueHandler extends HttpEntityMethodProcessor {

    public ApiResultReturnValueHandler(
        final List<HttpMessageConverter<?>> converters,
        final List<Object> requestResponseBodyAdvice
    ) {
        super(converters, requestResponseBodyAdvice);
    }

    @Override
    public boolean supportsReturnType(final MethodParameter returnType) {
        return (
            ApiResult.class.isAssignableFrom(returnType.getParameterType()) ||
            HeadersBuilder.class.isAssignableFrom(returnType.getParameterType()) ||
            returnType.hasMethodAnnotation(ApiResultBody.class)
        );
    }

    @Override
    public void handleReturnValue(
        @Nullable Object returnValue,
        final MethodParameter returnType,
        @NotNull final ModelAndViewContainer mavContainer,
        @NotNull final NativeWebRequest webRequest
    ) throws Exception {
        final ApiResultBody annotation = returnType.getMethodAnnotation(ApiResultBody.class);
        if (annotation != null && !(returnValue instanceof HeadersBuilder) && !(returnValue instanceof ApiResult)) {
            int status = annotation.status();
            String message = annotation.message();
            if (status == -1) {
                status = annotation.apiMessage().value();
            }
            if (message.isEmpty()) {
                message = annotation.apiMessage().message();
            }
            returnValue = ApiResult.status(status, message).data(returnValue);
        }
        if (returnValue instanceof HeadersBuilder) {
            returnValue = ((HeadersBuilder<?>) returnValue).build();
        }
        if (returnValue instanceof ApiResult) {
            returnValue = ((ApiResult<?>) returnValue).toResponseEntity();
        }
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
