/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.repository.NoteRepository;
import me.ixk.hoshi.note.view.NoteView;
import me.ixk.hoshi.security.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
