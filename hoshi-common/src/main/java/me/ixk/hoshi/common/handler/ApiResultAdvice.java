package me.ixk.hoshi.common.handler;

import java.util.List;
import java.util.stream.Collectors;
import me.ixk.hoshi.common.result.ApiResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一响应处理
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 2:52
 */
@RestControllerAdvice(basePackages = "me.ixk.hoshi")
public class ApiResultAdvice {

    @ExceptionHandler(Exception.class)
    public Object beforeBodyWriteException(final Exception e) {
        return ApiResult.error(e.getMessage()).build().toResponseEntity();
    }

    @ExceptionHandler(BindException.class)
    public Object validateException(final BindException e) {
        final List<String> errors = e
            .getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
        return ApiResult.badRequest().data(errors).toResponseEntity();
    }
}
