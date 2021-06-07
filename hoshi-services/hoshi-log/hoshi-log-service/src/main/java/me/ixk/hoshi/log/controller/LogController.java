/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.log.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.log.entity.Log;
import me.ixk.hoshi.log.repository.LogRepository;
import me.ixk.hoshi.log.view.request.AddLogView;
import me.ixk.hoshi.log.view.response.LogView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Otstar Lin
 * @date 2021/6/4 22:12
 */
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
@Api("日志控制器")
public class LogController {

    private final LogRepository logRepository;

    @PostMapping("")
    @ApiOperation("添加日志")
    public ApiResult<LogView> add(@Valid @JsonModel final AddLogView vo) {
        final Log log = this.logRepository.save(Log.ofAdd(vo));
        return ApiResult.ok(log.toView(), "保存日志成功");
    }
}
