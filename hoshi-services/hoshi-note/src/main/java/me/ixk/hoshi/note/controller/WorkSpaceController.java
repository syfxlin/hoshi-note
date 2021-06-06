/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.api.view.request.note.AddWorkSpaceView;
import me.ixk.hoshi.api.view.request.note.UpdateWorkSpaceView;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.note.entity.WorkSpace;
import me.ixk.hoshi.note.repository.WorkSpaceRepository;
import me.ixk.hoshi.security.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/27 17:35
 */
@RestController
@RequestMapping("/api/workspace")
@Api("工作区控制器")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class WorkSpaceController {

    private final WorkSpaceRepository workspaceRepository;

    @ModelAttribute
    public WorkSpaceRepository initBinder() {
        return this.workspaceRepository;
    }

    @GetMapping("")
    @ApiOperation("获取所有工作区")
    public ApiResult<ApiPage<WorkSpace>> list(@UserId final String userId, final Pageable page) {
        return ApiResult.page(this.workspaceRepository.findByUserId(userId, page), "获取所有工作区成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("获取工作区")
    public ApiResult<Object> get(@PathVariable("id") final String id, @UserId final String userId) {
        final Optional<WorkSpace> workspace = this.workspaceRepository.findByUserIdAndId(userId, id);
        if (workspace.isEmpty()) {
            return ApiResult.notFound("工作区未找到").build();
        }
        return ApiResult.ok(workspace.get(), "获取工作区成功");
    }

    @PostMapping("")
    @ApiOperation("添加工作区")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<WorkSpace> add(@UserId final String userId, @Valid @JsonModel final AddWorkSpaceView vo) {
        final WorkSpace workspace = WorkSpace.ofAdd(vo);
        workspace.setUserId(userId);
        return ApiResult.ok(this.workspaceRepository.save(workspace), "添加成功");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新工作区")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> update(@UserId final String userId, @Valid @JsonModel final UpdateWorkSpaceView vo) {
        if (this.workspaceRepository.findByUserIdAndId(userId, vo.getId()).isEmpty()) {
            return ApiResult.notFound("工作区未找到无法更新").build();
        }
        final WorkSpace workspace = WorkSpace.ofUpdate(vo);
        return ApiResult.ok(this.workspaceRepository.update(workspace), "更新工作区成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除工作区")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> delete(@PathVariable("id") final String id) {
        final Optional<WorkSpace> workspace = this.workspaceRepository.findById(id);
        if (workspace.isEmpty()) {
            return ApiResult.notFound("工作区不存在").build();
        }
        this.workspaceRepository.deleteById(id);
        return ApiResult.ok("删除工作区成功").build();
    }
}
