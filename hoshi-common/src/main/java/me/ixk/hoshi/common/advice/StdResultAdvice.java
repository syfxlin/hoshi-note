package me.ixk.hoshi.common.advice;

import java.util.List;
import java.util.stream.Collectors;
import me.ixk.hoshi.common.annotation.NoStdResult;
import me.ixk.hoshi.common.result.Result;
import me.ixk.hoshi.common.result.ResultCode;
import me.ixk.hoshi.common.util.Json;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应处理
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 2:52
 */
@RestControllerAdvice(annotations = RestController.class)
public class StdResultAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(final MethodParameter returnType, final Class converterType) {
        return (
            !returnType.getParameterType().equals(Result.class) && !returnType.hasMethodAnnotation(NoStdResult.class)
        );
    }

    @Override
    public Object beforeBodyWrite(
        Object body,
        final MethodParameter returnType,
        final MediaType selectedContentType,
        final Class selectedConverterType,
        final ServerHttpRequest request,
        final ServerHttpResponse response
    ) {
        if (MediaType.APPLICATION_JSON.equals(selectedContentType)) {
            body = Result.data(ResultCode.OK, body);
            if (returnType.getGenericParameterType().equals(String.class)) {
                body = Json.stringify(body);
            }
        }
        return body;
    }

    @ExceptionHandler(Exception.class)
    public Object beforeBodyWriteException(final Exception e) {
        return Result.error(
            ResultCode.ERROR.getCode(),
            e.getMessage() == null ? ResultCode.ERROR.getMessage() : e.getMessage()
        );
    }

    @ExceptionHandler(BindException.class)
    public Object validateException(final BindException e) {
        final List<String> errors = e
            .getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(Result.error(ResultCode.BAD_REQUEST, errors));
    }
}
