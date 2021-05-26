package me.ixk.hoshi.common.resolver;

import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonParam;
import me.ixk.hoshi.common.annotation.RequestJson;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Json 格式参数解析器
 *
 * @author Otstar Lin
 * @date 2020/11/17 下午 5:47
 */
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
        final NativeWebRequest nativeWebRequest,
        final WebDataBinderFactory webDataBinderFactory
    ) throws Exception {
        final DocumentContext context = this.getJsonBody(nativeWebRequest);
        final JsonParam jsonParam = methodParameter.getParameterAnnotation(JsonParam.class);
        Object value;
        if (jsonParam != null && !"".equals(jsonParam.path())) {
            value = context.read(jsonParam.path());
        } else {
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
                    value = TextNode.valueOf(jsonParam.defaultValue());
                }
            } else {
                return null;
            }
        }
        return this.conversionService.convert(value, methodParameter.getParameterType());
    }

    private DocumentContext getJsonBody(final NativeWebRequest nativeWebRequest) {
        final HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        final DocumentContext context = (DocumentContext) nativeWebRequest.getAttribute(
            JSON_REQUEST_ATTRIBUTE_NAME,
            NativeWebRequest.SCOPE_REQUEST
        );
        if (context == null) {
            try {
                if (servletRequest != null) {
                    JsonPath.parse(servletRequest.getInputStream());
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
