/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.resolver;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonParam;
import me.ixk.hoshi.common.annotation.RequestJson;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Json 格式参数解析器
 * <p>
 * 标注了 {@link RequestJson} 或 {@link JsonParam} 注解的才会处理
 *
 * @author Otstar Lin
 * @date 2020/11/17 下午 5:47
 */
@Component
@RequiredArgsConstructor
public class JsonArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_REQUEST_ATTRIBUTE_NAME = "JSON_REQUEST_BODY";
    private final ConversionService conversionService;

    @Override
    public boolean supportsParameter(final MethodParameter methodParameter) {
        return (
            methodParameter.hasParameterAnnotation(JsonParam.class) ||
            methodParameter.hasMethodAnnotation(RequestJson.class)
        );
    }

    @Override
    public Object resolveArgument(
        final MethodParameter methodParameter,
        final ModelAndViewContainer modelAndViewContainer,
        @NotNull final NativeWebRequest nativeWebRequest,
        final WebDataBinderFactory webDataBinderFactory
    ) throws Exception {
        final DocumentContext context = this.getJsonBody(nativeWebRequest);
        final JsonParam jsonParam = methodParameter.getParameterAnnotation(JsonParam.class);
        Object value;
        if (jsonParam != null && !"".equals(jsonParam.path())) {
            // 设置了 path 则使用 JsonPath 读取值
            value = context.read(jsonParam.path());
        } else {
            // 否则默认以参数名称作为 path 读取
            value = context.read(methodParameter.getParameterName());
        }
        if (value == null) {
            if (jsonParam != null) {
                if (jsonParam.required()) {
                    throw new MissingServletRequestParameterException(
                        jsonParam.path(),
                        methodParameter.getParameterType().getTypeName()
                    );
                } else {
                    value = jsonParam.defaultValue();
                }
            } else {
                return null;
            }
        }
        return this.conversionService.convert(value, methodParameter.getParameterType());
    }

    /**
     * 解析 Json，同时将解析后的 {@link DocumentContext} 存放于 Request Attribute 中
     *
     * @param nativeWebRequest {@link NativeWebRequest}
     * @return {@link DocumentContext}
     */
    private DocumentContext getJsonBody(final NativeWebRequest nativeWebRequest) {
        final HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        DocumentContext context = (DocumentContext) nativeWebRequest.getAttribute(
            JSON_REQUEST_ATTRIBUTE_NAME,
            NativeWebRequest.SCOPE_REQUEST
        );
        if (context == null) {
            try {
                if (servletRequest != null) {
                    context = JsonPath.parse(servletRequest.getInputStream());
                }
            } catch (final IOException e) {
                //
            }
            if (context != null) {
                nativeWebRequest.setAttribute(JSON_REQUEST_ATTRIBUTE_NAME, context, NativeWebRequest.SCOPE_REQUEST);
            }
        }

        return context;
    }
}
