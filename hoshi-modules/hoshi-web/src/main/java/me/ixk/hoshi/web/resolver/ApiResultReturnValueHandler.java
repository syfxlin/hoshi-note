/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.resolver;

import java.util.List;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.ApiResult.HeadersBuilder;
import me.ixk.hoshi.web.result.ApiResultUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

/**
 * {@link ApiResult} 响应处理器
 * <p>
 * 返回值是 {@link ApiResult}，{@link HeadersBuilder} 及其子类
 *
 * @author Otstar Lin
 * @date 2021/6/10 21:44
 */
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
            HeadersBuilder.class.isAssignableFrom(returnType.getParameterType())
        );
    }

    @Override
    public void handleReturnValue(
        @Nullable Object returnValue,
        @NotNull final MethodParameter returnType,
        @NotNull final ModelAndViewContainer mavContainer,
        @NotNull final NativeWebRequest webRequest
    ) throws Exception {
        // 如果是 HeadersBuilder，说明当前还处于构造器状态，需要调用 build 方法构造
        if (returnValue instanceof HeadersBuilder r) {
            returnValue = r.build();
        }
        // 如果是 ApiResult，则将返回值替换成处理后的 ResponseEntity，委托给父类（HttpEntityMethodProcessor）进行处理
        if (returnValue instanceof ApiResult r) {
            returnValue = ApiResultUtil.toResponseEntity(r);
        }
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
