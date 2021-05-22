package me.ixk.hoshi.common.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import me.ixk.hoshi.common.annotation.JsonParam;
import me.ixk.hoshi.common.annotation.RequestJson;
import me.ixk.hoshi.common.util.Data;
import me.ixk.hoshi.common.util.Json;
import org.springframework.core.MethodParameter;
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
public class JsonArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_REQUEST_ATTRIBUTE_NAME = "JSON_REQUEST_BODY";
    private static final String BODY_VALUE_NAME = "&BODY";

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
        final JsonNode body = this.getJsonBody(nativeWebRequest);
        if (BODY_VALUE_NAME.equals(methodParameter.getParameterName())) {
            return Json.convertToObject(body, methodParameter.getParameterType());
        }
        final JsonParam jsonParam = methodParameter.getParameterAnnotation(JsonParam.class);
        JsonNode node;
        if (jsonParam != null && !"".equals(jsonParam.name())) {
            node = Data.dataGet(body, jsonParam.name(), NullNode.getInstance());
        } else {
            node = body.get(methodParameter.getParameterName());
        }
        if (node.isNull()) {
            if (jsonParam != null) {
                if (jsonParam.required()) {
                    throw new MissingServletRequestParameterException(
                        jsonParam.name(),
                        methodParameter.getParameterType().getTypeName()
                    );
                } else {
                    node = TextNode.valueOf(jsonParam.defaultValue());
                }
            } else {
                return null;
            }
        }
        return Json.convertToObject(node, methodParameter.getParameterType());
    }

    private JsonNode getJsonBody(final NativeWebRequest nativeWebRequest) {
        final HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        JsonNode body = (JsonNode) nativeWebRequest.getAttribute(
            JSON_REQUEST_ATTRIBUTE_NAME,
            NativeWebRequest.SCOPE_REQUEST
        );

        if (body == null) {
            try {
                if (servletRequest != null) {
                    body = Json.parse(servletRequest.getReader().lines().collect(Collectors.joining("\n")));
                }
            } catch (final IOException e) {
                //
            }
            if (body == null) {
                body = NullNode.getInstance();
            }
            nativeWebRequest.setAttribute(JSON_REQUEST_ATTRIBUTE_NAME, body, NativeWebRequest.SCOPE_REQUEST);
        }

        return body;
    }
}
