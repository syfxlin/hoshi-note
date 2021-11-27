/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.controller;

import cn.hutool.core.net.url.UrlBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.file.properties.PexelsProperties;
import me.ixk.hoshi.web.util.Json;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Pexels 图片
 *
 * @author Otstar Lin
 * @date 2021/11/27 13:09
 */
@RestController
@RequiredArgsConstructor
@Api("Pexels 控制器")
@RequestMapping("/pexels")
public class PexelsController {

    private final OkHttpClient client;
    private final PexelsProperties properties;

    @ApiOperation("精选图片")
    @GetMapping("/curated")
    @PreAuthorize("hasAuthority('PEXELS')")
    public DeferredResult<ApiResult<?>> curated(
        @RequestParam(value = "page", required = false) final Integer page,
        @RequestParam(value = "per_page", required = false) final Integer perPage
    ) {
        final UrlBuilder url = UrlBuilder.of("https://api.pexels.com/v1/curated");
        if (page != null) {
            url.addQuery("page", page.toString());
        }
        if (perPage != null) {
            url.addQuery("per_page", perPage.toString());
        }
        DeferredResult<ApiResult<?>> output = new DeferredResult<>();
        final Request request = new Request.Builder()
            .get()
            .url(url.build())
            .addHeader("Authorization", properties.getToken())
            .build();
        Call call = client.newCall(request);
        call.enqueue(
            new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    output.setResult(ApiResult.error(e.getMessage(), "从 pexels 获取图片失败"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    JsonNode node = Json.parse(Objects.requireNonNull(response.body()).string());
                    output.setResult(ApiResult.ok(node.get("photos"), "获取精选图片成功"));
                }
            }
        );
        return output;
    }

    @ApiOperation("搜索图片")
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('PEXELS')")
    public DeferredResult<ApiResult<?>> curated(
        @RequestParam("query") final String query,
        @RequestParam(value = "page", required = false) final Integer page,
        @RequestParam(value = "per_page", required = false) final Integer perPage
    ) {
        final UrlBuilder url = UrlBuilder.of("https://api.pexels.com/v1/search");
        url.addQuery("query", query);
        if (page != null) {
            url.addQuery("page", page.toString());
        }
        if (perPage != null) {
            url.addQuery("per_page", perPage.toString());
        }
        DeferredResult<ApiResult<?>> output = new DeferredResult<>();
        final Request request = new Request.Builder()
            .get()
            .url(url.build())
            .addHeader("Authorization", properties.getToken())
            .build();
        Call call = client.newCall(request);
        call.enqueue(
            new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    output.setResult(ApiResult.error(e.getMessage(), "从 pexels 获取图片失败"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    JsonNode node = Json.parse(Objects.requireNonNull(response.body()).string());
                    output.setResult(ApiResult.ok(node.get("photos"), "搜索图片成功"));
                }
            }
        );
        return output;
    }
}
