/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.web.result;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.ixk.hoshi.common.result.ApiEntity;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.web.util.Json;
import org.springframework.http.ResponseEntity;

/**
 * @author Otstar Lin
 * @date 2021/11/14 16:36
 */
public final class ApiResultUtil {

    private ApiResultUtil() {}

    /**
     * 转换为 {@link JsonNode}
     *
     * @return
     */
    public static <T> JsonNode toJsonNode(ApiResult<T> result) {
        return Json.convertToNode(result.toEntity());
    }

    /**
     * 转换为 {@link ResponseEntity}
     * <p>
     * 为了避免重复造轮子，只需要将 {@link ApiResult} 直接转换成 {@link ResponseEntity}
     *
     * @return
     */
    public static <T> ResponseEntity<ApiEntity<T>> toResponseEntity(ApiResult<T> result) {
        return ResponseEntity.status(result.getStatus()).headers(result.getHeaders()).body(result.toEntity());
    }

    /**
     * 写入 {@link HttpServletResponse}
     * <p>
     * 部分情况下需要直接写入 {@link HttpServletResponse}，则调用对应的方法进行写入操作
     *
     * @param response {@link HttpServletResponse}
     * @return {@link HttpServletResponse}
     * @throws IOException 异常
     */
    public static <T> HttpServletResponse toResponse(ApiResult<T> result, final HttpServletResponse response)
        throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(result.getStatus());
        result.getHeaders().forEach((key, value) -> value.forEach(v -> response.addHeader(key, v)));
        response.getWriter().write(toJsonNode(result).toString());
        return response;
    }
}
