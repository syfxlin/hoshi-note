/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.mysql.util.Jpa;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.NoteHistory;
import me.ixk.hoshi.note.entity.Workspace;
import me.ixk.hoshi.note.repository.NoteHistoryRepository;
import me.ixk.hoshi.note.repository.NoteRepository;
import me.ixk.hoshi.note.repository.WorkspaceRepository;
import me.ixk.hoshi.note.request.AddNoteView;
import me.ixk.hoshi.note.request.UpdateNoteView;
import me.ixk.hoshi.note.response.BreadcrumbView;
import me.ixk.hoshi.note.response.BreadcrumbView.Item;
import me.ixk.hoshi.web.annotation.JsonModel;
import me.ixk.hoshi.web.annotation.UserId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
    private final NoteHistoryRepository noteHistoryRepository;

    @GetMapping(value = { "/list/{workspaceId}", "/list/{workspaceId}/{parentId}" })
    @ApiOperation("获取笔记列表")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> list(
        @UserId final Long userId,
        @PathVariable("workspaceId") final String workspaceId,
        @PathVariable(value = "parentId", required = false) final String parentId
    ) {
        final Optional<Workspace> optional = workspaceRepository.findByIdAndUser(workspaceId, userId);
        if (optional.isEmpty()) {
            return ApiResult.bindException("工作区不存在");
        }
        if (parentId != null && noteRepository.findByWorkspaceIdAndId(workspaceId, parentId).isEmpty()) {
            return ApiResult.bindException("父笔记不存在");
        }
        return ApiResult.ok(
            noteRepository
                .findByWorkspaceIdAndParentId(workspaceId, parentId)
                .stream()
                .map(Note::toListView)
                .collect(Collectors.toList()),
            "获取笔记列表成功"
        );
    }

    @PostMapping(value = { "/{workspaceId}", "/{workspaceId}/{parentId}" })
    @ApiOperation("添加笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
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
        return ApiResult.ok(noteRepository.save(add).toView(), "添加笔记成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("获取笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> get(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> note = noteRepository.findById(id);
        if (note.isEmpty() || !userId.equals(note.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        return ApiResult.ok(note.get().toView(), "获取笔记成功");
    }

    @GetMapping("/{id}/breadcrumb")
    @ApiOperation("获取笔记面包屑")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> breadcrumb(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> optional = noteRepository.findById(id);
        if (optional.isEmpty() || !userId.equals(optional.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note note = optional.get();
        final Item workspace = Item
            .builder()
            .id(note.getWorkspace().getId())
            .name(note.getWorkspace().getName())
            .build();
        final LinkedList<Item> parent = new LinkedList<>();
        Note curr = note.getParent();
        while (curr != null) {
            parent.addFirst(Item.builder().id(curr.getId()).name(curr.getName()).build());
            curr = curr.getParent();
        }
        final List<Item> children = noteRepository
            .findByWorkspaceIdAndParentId(note.getWorkspace().getId(), note.getId())
            .stream()
            .map(n -> Item.builder().id(n.getId()).name(n.getName()).build())
            .collect(Collectors.toList());
        return ApiResult.ok(
            BreadcrumbView.builder().workspace(workspace).parent(parent).children(children).build(),
            "获取笔记面包屑成功"
        );
    }

    @PutMapping("/{id}")
    @ApiOperation("修改笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> update(@UserId final Long userId, @JsonModel @Valid final UpdateNoteView vo) {
        final Optional<Note> note = noteRepository.findById(vo.getId());
        if (note.isEmpty() || !userId.equals(note.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note update = Note.ofUpdate(vo);
        final Note origin = note.get();
        final boolean needSaveHistory = NoteHistory.needSave(update, origin);
        final Note merge = Jpa.merge(update, origin);
        // workspace
        final String workspaceId = vo.getWorkspace();
        if (workspaceId != null) {
            final Optional<Workspace> workspace = workspaceRepository.findByIdAndUser(workspaceId, userId);
            if (workspace.isEmpty()) {
                return ApiResult.bindException("工作区不存在");
            }
            merge.setWorkspace(workspace.get());
        }
        // parent
        final String parentId = vo.getParent();
        if (parentId != null) {
            if ("null".equals(parentId)) {
                merge.setParent(null);
            } else {
                final Optional<Note> parent = noteRepository.findByWorkspaceIdAndId(
                    workspaceId == null ? merge.getWorkspace().getId() : workspaceId,
                    parentId
                );
                if (parent.isEmpty()) {
                    return ApiResult.bindException("父笔记不存在");
                }
                merge.setParent(parent.get());
            }
        }
        // version
        merge.setVersion(merge.getVersion() + 1);
        // save
        final Note save = noteRepository.save(merge);
        // history
        if (needSaveHistory) {
            noteHistoryRepository.save(NoteHistory.of(save));
        }
        return ApiResult.ok(save.toView(), "修改笔记成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> delete(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> note = noteRepository.findById(id);
        if (note.isEmpty() || !userId.equals(note.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        noteRepository.deleteById(note.get().getId());
        return ApiResult.ok("删除笔记成功").build();
    }
}
