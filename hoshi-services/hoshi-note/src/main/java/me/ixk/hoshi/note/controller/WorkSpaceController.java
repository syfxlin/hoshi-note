/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.note.entity.WorkSpace;
import me.ixk.hoshi.note.repository.WorkSpaceRepository;
import me.ixk.hoshi.note.view.AddWorkSpaceView;
import me.ixk.hoshi.security.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Otstar Lin
 * @date 2021/5/27 17:35
 */
@RestController
@RequestMapping("/api/workspace")
@Api("工作区控制器")
@RequiredArgsConstructor
public class WorkSpaceController {

    private final WorkSpaceRepository workspaceRepository;

    @GetMapping("")
    @ApiOperation("获取所有工作区")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<ApiPage<WorkSpace>> list(@UserId final String userId, final Pageable page) {
        return ApiResult.page(this.workspaceRepository.findByUserIdEquals(userId, page), "获取所有工作区成功");
    }

    @PostMapping("")
    @ApiOperation("添加工作区")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> add(@UserId final String userId, final AddWorkSpaceView vo) {
        final WorkSpace workspace = vo.toEntity();
        workspace.setUserId(userId);
        return ApiResult.ok(this.workspaceRepository.save(workspace), "添加成功");
    }
}
