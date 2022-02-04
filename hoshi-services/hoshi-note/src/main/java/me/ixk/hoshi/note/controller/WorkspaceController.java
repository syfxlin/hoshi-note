/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mysql.util.Jpa;
import me.ixk.hoshi.note.entity.Workspace;
import me.ixk.hoshi.note.repository.WorkspaceRepository;
import me.ixk.hoshi.note.request.AddWorkspaceView;
import me.ixk.hoshi.note.request.UpdateWorkspaceView;
import me.ixk.hoshi.web.annotation.JsonModel;
import me.ixk.hoshi.web.annotation.UserId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工作区控制器
 *
 * @author Otstar Lin
 * @date 2021/11/18 15:07
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "工作区控制器")
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceRepository workspaceRepository;

    @GetMapping("")
    @Operation(summary = "获取工作区列表")
    @PreAuthorize("hasAuthority('WORKSPACE')")
    public ApiResult<?> list(@UserId final Long userId) {
        return ApiResult.ok(
            workspaceRepository.findByUser(userId).stream().map(Workspace::toView).collect(Collectors.toList()),
            "获取工作区列表成功"
        );
    }

    @PostMapping("")
    @Operation(summary = "添加工作区")
    @PreAuthorize("hasAuthority('WORKSPACE')")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public ApiResult<?> add(@UserId final Long userId, @JsonModel @Valid final AddWorkspaceView vo) {
        if (vo.getDomain() != null && workspaceRepository.findByDomain(vo.getDomain()).isPresent()) {
            return ApiResult.bindException("域名已经存在，请更换域名");
        }
        return ApiResult.ok(workspaceRepository.save(Workspace.ofAdd(vo, userId)), "添加工作区成功");
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改工作区")
    @PreAuthorize("hasAuthority('WORKSPACE')")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public ApiResult<?> update(@UserId final Long userId, @JsonModel @Valid final UpdateWorkspaceView vo) {
        Optional<Workspace> optional = workspaceRepository.findByIdAndUser(vo.getId(), userId);
        if (optional.isEmpty()) {
            return ApiResult.bindException("工作区不存在");
        }
        if (
            vo.getDomain() != null &&
                !vo.getDomain().equals(optional.get().getDomain()) &&
                workspaceRepository.findByDomain(vo.getDomain()).isPresent()
        ) {
            return ApiResult.bindException("域名已经存在，请更换域名");
        }
        Workspace workspace = optional.get();
        return ApiResult.ok(workspaceRepository.save(Jpa.merge(Workspace.ofUpdate(vo), workspace)), "修改工作区成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除工作区")
    @PreAuthorize("hasAuthority('WORKSPACE')")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public ApiResult<?> delete(@UserId final Long userId, @PathVariable("id") final String id) {
        Optional<Workspace> optional = workspaceRepository.findByIdAndUser(id, userId);
        if (optional.isEmpty()) {
            return ApiResult.bindException("工作区不存在");
        }
        workspaceRepository.deleteById(optional.get().getId());
        return ApiResult.ok("删除工作区成功").build();
    }
}
