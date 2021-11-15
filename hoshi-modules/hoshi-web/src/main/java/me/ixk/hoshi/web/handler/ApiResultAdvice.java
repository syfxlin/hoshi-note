/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.handler;

import me.ixk.hoshi.common.result.ApiBindException;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.web.result.ApiResultUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

    /**
     * 异常默认处理器
     *
     * @param e 异常
     * @return 处理后的响应
     * @throws Exception 再次抛出的异常
     */
    @ExceptionHandler(Exception.class)
    public Object globalException(final Exception e) throws Exception {
        // 忽略 Spring Security 认证异常，否则 Spring Security 配置的处理器将失效
        if (e instanceof AccessDeniedException || e instanceof AuthenticationException) {
            throw e;
        }
        return ApiResultUtil.toResponseEntity(ApiResult.error(e.getMessage()).build());
    }

    /**
     * 参数绑定异常处理器
     *
     * @param e 参数绑定异常
     * @return 处理后的响应
     */
    @ExceptionHandler(BindException.class)
    public Object validateException(final BindException e) {
        return ApiResultUtil.toResponseEntity(ApiResult.bindException(e));
    }

    /**
     * Api 参数绑定异常处理器
     *
     * @param e 参数绑定异常
     * @return 处理后的响应
     */
    @ExceptionHandler(ApiBindException.class)
    public Object validateException(final ApiBindException e) {
        final String message = e.getMessage();
        if (message == null) {
            return ApiResultUtil.toResponseEntity(ApiResult.bindException(e.getErrors()));
        } else {
            return ApiResultUtil.toResponseEntity(ApiResult.bindException(message, e.getErrors()));
        }
    }
}
