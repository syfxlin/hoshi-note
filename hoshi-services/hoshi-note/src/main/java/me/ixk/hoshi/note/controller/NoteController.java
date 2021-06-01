/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import static me.ixk.hoshi.note.entity.NoteHistory.MIN_UPDATED;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.OffsetDateTime;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.NoteHistory;
import me.ixk.hoshi.note.entity.WorkSpace;
import me.ixk.hoshi.note.repository.NoteHistoryRepository;
import me.ixk.hoshi.note.repository.NoteRepository;
import me.ixk.hoshi.note.repository.WorkSpaceRepository;
import me.ixk.hoshi.note.view.AddNoteView;
import me.ixk.hoshi.note.view.NoteView;
import me.ixk.hoshi.note.view.UpdateNoteView;
import me.ixk.hoshi.security.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/30 16:26
 */
@RestController
@RequestMapping("/api/notes")
@Api("笔记控制器")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class NoteController {

    private final NoteRepository noteRepository;
    private final WorkSpaceRepository workspaceRepository;
    private final NoteHistoryRepository noteHistoryRepository;

    @GetMapping("/{workspaceId}")
    @ApiOperation("获取笔记列表")
    public ApiResult<ApiPage<NoteView>> list(
        @UserId final String userId,
        @PathVariable("workspaceId") final String workspaceId,
        final Pageable page
    ) {
        return ApiResult.page(
            this.noteRepository.findByUserIdAndWorkspaceId(userId, workspaceId, page).map(NoteView::of),
            "获取笔记列表成功"
        );
    }

    @GetMapping("/{workspaceId}/{noteId}")
    @ApiOperation("获取笔记")
    public ApiResult<Object> get(
        @UserId final String userId,
        @PathVariable("workspaceId") final String workspaceId,
        @PathVariable("noteId") final String noteId
    ) {
        final Optional<Note> note =
            this.noteRepository.findByUserIdAndWorkSpaceIdAndNoteId(userId, workspaceId, noteId);
        if (note.isEmpty()) {
            return ApiResult.notFound("指定笔记未找到").build();
        }
        return ApiResult.ok(note.get(), "获取笔记成功");
    }

    @PostMapping("/{workspaceId}")
    @ApiOperation("添加笔记")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> add(@UserId final String userId, @Valid @JsonModel final AddNoteView vo) {
        final String workspaceId = vo.getWorkspaceId();
        final Optional<WorkSpace> workspace = this.workspaceRepository.findByUserIdAndId(userId, workspaceId);
        if (workspace.isEmpty()) {
            return ApiResult.notFound("指定工作区未找到").build();
        }
        final Note note = vo.toEntity();
        note.setWorkspace(workspace.get());
        if (vo.getParentId() != null) {
            final Optional<Note> parent =
                this.noteRepository.findByUserIdAndWorkSpaceIdAndNoteId(userId, workspaceId, vo.getParentId());
            if (parent.isEmpty()) {
                return ApiResult.notFound("父笔记未找到").build();
            }
            note.setParent(parent.get());
        }
        return ApiResult.ok(this.noteRepository.save(note), "添加笔记成功");
    }

    @PutMapping("/{workspaceId}/{id}")
    @ApiOperation("更新笔记")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> update(@UserId final String userId, @Valid @JsonModel final UpdateNoteView vo) {
        final String workspaceId = vo.getWorkspaceId();
        final Optional<WorkSpace> workspace = this.workspaceRepository.findByUserIdAndId(userId, workspaceId);
        if (workspace.isEmpty()) {
            return ApiResult.notFound("指定工作区未找到").build();
        }
        final Optional<Note> origin =
            this.noteRepository.findByUserIdAndWorkSpaceIdAndNoteId(userId, workspaceId, vo.getId());
        if (origin.isEmpty()) {
            return ApiResult.notFound("原始笔记未找到").build();
        }
        final Note note = vo.toEntity();
        note.setWorkspace(workspace.get());
        if (vo.getParentId() != null) {
            final Optional<Note> parent =
                this.noteRepository.findByUserIdAndWorkSpaceIdAndNoteId(userId, workspaceId, vo.getParentId());
            if (parent.isEmpty()) {
                return ApiResult.notFound("父笔记未找到").build();
            }
            note.setParent(parent.get());
        }
        final Note originNote = origin.get();
        note.setVersion(originNote.getVersion() + 1);
        final OffsetDateTime lastTime = originNote.getUpdatedTime();
        final OffsetDateTime nowTime = note.getUpdatedTime();
        if (
            lastTime.isBefore(nowTime.minusMinutes(MIN_UPDATED)) && !originNote.getContent().equals(note.getContent())
        ) {
            this.noteHistoryRepository.save(NoteHistory.of(originNote));
        }
        return ApiResult.ok(this.noteRepository.update(note), "更新成功");
    }

    @DeleteMapping("/{workspaceId}/{noteId}")
    @ApiOperation("删除笔记")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> delete(
        @UserId final String userId,
        @PathVariable("workspaceId") final String workspaceId,
        @PathVariable("noteId") final String noteId
    ) {
        final Optional<Note> note =
            this.noteRepository.findByUserIdAndWorkSpaceIdAndNoteId(userId, workspaceId, noteId);
        if (note.isEmpty()) {
            return ApiResult.notFound("笔记未找到").build();
        }
        this.noteRepository.deleteById(note.get().getId());
        return ApiResult.ok("删除成功").build();
    }
}
