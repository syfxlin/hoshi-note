package me.ixk.hoshi.common.handler;

import me.ixk.hoshi.common.result.ApiBindException;
import me.ixk.hoshi.common.result.ApiResult;
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
        return ApiResult.bindException(e).toResponseEntity();
    }

    @ExceptionHandler(ApiBindException.class)
    public Object validateException(final ApiBindException e) {
        final String message = e.getMessage();
        if (message == null) {
            return ApiResult.bindException(e.getErrors()).toResponseEntity();
        } else {
            return ApiResult.bindException(message, e.getErrors()).toResponseEntity();
        }
    }
}
