/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.Workspace;
import me.ixk.hoshi.note.repository.NoteRepository;
import me.ixk.hoshi.note.repository.WorkspaceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

/**
 * 分享控制器
 *
 * @author Otstar Lin
 * @date 2021/11/29 16:13
 */
@RestController
@RequiredArgsConstructor
@Api("分享控制器")
@RequestMapping("/shares")
public class ShareController {

    private final WorkspaceRepository workspaceRepository;
    private final NoteRepository noteRepository;

    @ApiOperation("获取已分享的笔记列表")
    @GetMapping("/list/{userId:\\d+}")
    public ApiResult<?> list(
        @PathVariable("userId") final Long userId,
        final Pageable page,
        @RequestParam(value = "search", required = false) final String search
    ) {
        final Set<Workspace> workspaces = workspaceRepository.findByUser(userId);
        final Specification<Note> specification = (root, query, cb) -> {
            final Predicate predicate = cb.and(
                root.get("workspace").in(workspaces),
                cb.equal(root.get("status"), Note.Status.NORMAL),
                cb.equal(root.get("share"), true)
            );
            if (search != null) {
                return cb.and(
                    predicate,
                    cb.or(
                        cb.like(root.get("name"), String.format("%%%s%%", search)),
                        cb.like(root.get("content"), String.format("%%%s%%", search)),
                        cb.like(root.get("attributes"), String.format("%%%s%%", search)),
                        cb.like(root.get("createdTime"), String.format("%%%s%%", search)),
                        cb.like(root.get("updatedTime"), String.format("%%%s%%", search))
                    )
                );
            } else {
                return predicate;
            }
        };
        return ApiResult.page(
            noteRepository.findAll(specification, page).map(Note::toListView),
            "获取已分享笔记列表成功"
        );
    }

    @GetMapping("/{id}")
    @ApiOperation("获取已分享笔记")
    public ApiResult<?> get(@PathVariable("id") final String id) {
        final Optional<Note> note = noteRepository.findByIdAndShareIsTrue(id);
        if (note.isEmpty()) {
            return ApiResult.bindException("笔记不存在，或用户未分享该笔记");
        }
        return ApiResult.ok(note.get().toView(), "获取已分享笔记成功");
    }

    @GetMapping("/{id}/children")
    @ApiOperation("获取已分享笔记的子笔记")
    public ApiResult<?> children(@PathVariable("id") final String id) {
        final Optional<Note> note = noteRepository.findByIdAndShareIsTrue(id);
        if (note.isEmpty()) {
            return ApiResult.bindException("笔记不存在，或用户未分享该笔记");
        }
        return ApiResult.ok(
            note.get().getChildren().stream().map(Note::toListView).collect(Collectors.toList()),
            "获取已分享笔记成功"
        );
    }
}
