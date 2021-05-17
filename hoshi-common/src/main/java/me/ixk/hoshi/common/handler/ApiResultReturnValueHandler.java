package me.ixk.hoshi.common.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import me.ixk.hoshi.common.annotation.ApiResultBody;
import me.ixk.hoshi.common.result.ApiResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 统一响应返回值处理器
 *
 * @author Otstar Lin
 * @date 2021/5/17 下午 8:03
 */
@Component
public class ApiResultReturnValueHandler implements HandlerMethodReturnValueHandler, InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    private HandlerMethodReturnValueHandler handler;

    @Override
    public boolean supportsReturnType(final MethodParameter returnType) {
        return (
            ApiResult.class.isAssignableFrom(returnType.getParameterType()) ||
            returnType.hasMethodAnnotation(ApiResultBody.class)
        );
    }

    @Override
    public void handleReturnValue(
        Object returnValue,
        final MethodParameter returnType,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest
    ) throws Exception {
        final ApiResultBody annotation = returnType.getMethodAnnotation(ApiResultBody.class);
        if (annotation != null) {
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
        if (returnValue instanceof ApiResult) {
            returnValue = ((ApiResult<?>) returnValue).toEntity();
        }
        handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final List<HandlerMethodReturnValueHandler> handlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        if (handlers == null) {
            return;
        }
        final Optional<HandlerMethodReturnValueHandler> handler = handlers
            .stream()
            .filter(h -> h instanceof HttpEntityMethodProcessor)
            .findFirst();
        handler.ifPresent(handlerMethodReturnValueHandler -> this.handler = handlerMethodReturnValueHandler);
        final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
        returnValueHandlers.add(this);
        returnValueHandlers.addAll(handlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(returnValueHandlers);
    }
}
