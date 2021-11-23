/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mysql.util.Jpa;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.Workspace;
import me.ixk.hoshi.note.repository.NoteRepository;
import me.ixk.hoshi.note.repository.WorkspaceRepository;
import me.ixk.hoshi.note.request.AddNoteView;
import me.ixk.hoshi.note.request.UpdateNoteView;
import me.ixk.hoshi.web.annotation.JsonModel;
import me.ixk.hoshi.web.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 笔记控制器
 *
 * @author Otstar Lin
 * @date 2021/11/18 15:07
 */
@RestController
@RequiredArgsConstructor
@Api("笔记控制器")
@RequestMapping("/notes")
public class NoteController {

    private final WorkspaceRepository workspaceRepository;
    private final NoteRepository noteRepository;

    @GetMapping(value = { "/{workspaceId}", "/{workspaceId}/{parentId}" })
    @ApiOperation("获取笔记列表")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> list(
        @UserId final Long userId,
        @PathVariable("workspaceId") final String workspaceId,
        @PathVariable(value = "parentId", required = false) final String parentId,
        final Pageable page
    ) {
        final Optional<Workspace> optional = workspaceRepository.findByIdAndUser(workspaceId, userId);
        if (optional.isEmpty()) {
            return ApiResult.bindException("工作区不存在");
        }
        if (parentId != null && noteRepository.findByWorkspaceIdAndId(workspaceId, parentId).isEmpty()) {
            return ApiResult.bindException("父笔记不存在");
        }
        return ApiResult.page(
            noteRepository.findByWorkspaceIdAndParentId(workspaceId, parentId, page).map(note -> note.toView(false)),
            "获取笔记列表成功"
        );
    }

    @PostMapping(value = { "/{workspaceId}", "/{workspaceId}/{parentId}" })
    @ApiOperation("添加笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> add(@UserId final Long userId, @JsonModel @Valid final AddNoteView vo) {
        final Optional<Workspace> workspace = workspaceRepository.findByIdAndUser(vo.getWorkspaceId(), userId);
        if (workspace.isEmpty()) {
            return ApiResult.bindException("工作区不存在");
        }
        final Optional<Note> parent = noteRepository.findByWorkspaceIdAndId(vo.getWorkspaceId(), vo.getParentId());
        if (vo.getParentId() != null && parent.isEmpty()) {
            return ApiResult.bindException("父笔记不存在");
        }
        final Note add = Note.ofAdd(vo);
        add.setWorkspace(workspace.get());
        add.setParent(parent.orElse(null));
        return ApiResult.ok(noteRepository.save(add).toView(false), "添加笔记成功");
    }

    @PutMapping("/{id}")
    @ApiOperation("修改笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> update(@UserId final Long userId, @JsonModel @Valid final UpdateNoteView vo) {
        final Optional<Note> note = noteRepository.findById(vo.getId());
        if (note.isEmpty() || !userId.equals(note.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note update = Note.ofUpdate(vo);
        String workspaceId = vo.getWorkspace();
        if (workspaceId != null) {
            Optional<Workspace> workspace = workspaceRepository.findByIdAndUser(workspaceId, userId);
            if (workspace.isEmpty()) {
                return ApiResult.bindException("工作区不存在");
            }
            update.setWorkspace(workspace.get());
        }
        String parentId = vo.getParent();
        if (parentId != null && !"null".equals(parentId)) {
            Optional<Note> parent = noteRepository.findByWorkspaceIdAndId(
                workspaceId == null ? note.get().getWorkspace().getId() : workspaceId,
                parentId
            );
            if (parent.isEmpty()) {
                return ApiResult.bindException("父笔记不存在");
            }
            update.setParent(parent.get());
        }
        Note merge = Jpa.merge(update, note.get());
        // fix: null parent
        if ("null".equals(parentId)) {
            merge.setParent(null);
        }
        return ApiResult.ok(noteRepository.save(merge).toView(true), "修改笔记成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> delete(@UserId final Long userId, @PathVariable("id") final String id) {
        Optional<Note> note = noteRepository.findById(id);
        if (note.isEmpty() || !userId.equals(note.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        noteRepository.deleteById(note.get().getId());
        return ApiResult.ok("删除笔记成功").build();
    }
}
