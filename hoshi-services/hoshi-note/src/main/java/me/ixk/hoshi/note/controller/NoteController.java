/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import cn.hutool.core.util.EnumUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
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
import me.ixk.hoshi.web.annotation.JsonModel;
import me.ixk.hoshi.web.annotation.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @GetMapping(value = { "/tree/{workspaceId}", "/tree/{workspaceId}/{parentId}" })
    @ApiOperation("获取笔记树")
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
                .findByWorkspaceIdAndParentIdAndStatus(workspaceId, parentId, Note.Status.NORMAL)
                .stream()
                .map(Note::toListView)
                .collect(Collectors.toList()),
            "获取笔记列表成功"
        );
    }

    @GetMapping("/search")
    @ApiOperation("搜索")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> search(
        @UserId final Long userId,
        final Pageable page,
        @RequestParam("search") final String search,
        @RequestParam(value = "filters", required = false) final List<String> filters
    ) {
        final Map<String, String> filterMap = new HashMap<>(5);
        if (filters != null) {
            for (final String filter : filters) {
                final String[] kv = filter.split("~");
                filterMap.put(kv[0].trim(), kv.length == 1 ? "true" : kv[1]);
            }
        }
        final Set<Workspace> workspaces = workspaceRepository.findByUser(userId);
        final Specification<Note> specification = (root, query, cb) -> {
            // only name
            final boolean onlyName = filterMap.containsKey("onlyName");
            Predicate predicate = onlyName
                ? cb.like(root.get("name"), String.format("%%%s%%", search))
                : cb.or(
                    cb.like(root.get("name"), String.format("%%%s%%", search)),
                    cb.like(root.get("content"), String.format("%%%s%%", search)),
                    cb.like(root.get("attributes"), String.format("%%%s%%", search))
                );
            // workspace filter
            final String fw = filterMap.get("workspace");
            if (fw == null) {
                predicate = cb.and(predicate, root.get("workspace").in(workspaces));
            } else {
                predicate = cb.and(predicate, cb.equal(root.get("workspace").get("id"), fw));
            }
            // status
            final String fs = filterMap.get("status");
            if (fs == null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), Note.Status.NORMAL));
            } else if (EnumUtil.contains(Note.Status.class, fs)) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), Note.Status.valueOf(fs)));
            }
            // createdTime
            final String cts = filterMap.get("createdTimeStart");
            if (cts != null) {
                predicate =
                    cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createdTime"), OffsetDateTime.parse(cts)));
            }
            final String cte = filterMap.get("createdTimeEnd");
            if (cte != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createdTime"), OffsetDateTime.parse(cte)));
            }
            // updatedTime
            final String uts = filterMap.get("updatedTimeStart");
            if (uts != null) {
                predicate =
                    cb.and(predicate, cb.greaterThanOrEqualTo(root.get("updatedTime"), OffsetDateTime.parse(uts)));
            }
            final String ute = filterMap.get("updatedTimeEnd");
            if (ute != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("updatedTime"), OffsetDateTime.parse(ute)));
            }
            return predicate;
        };
        return ApiResult.page(noteRepository.findAll(specification, page).map(Note::toListView), "搜索笔记列表成功");
    }

    @GetMapping("/list/{workspaceId}")
    @ApiOperation("获取工作区笔记列表")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> list(
        @UserId final Long userId,
        @PathVariable("workspaceId") final String workspaceId,
        final Pageable page,
        @RequestParam(value = "search", required = false) final String search
    ) {
        final Optional<Workspace> optional = workspaceRepository.findByIdAndUser(workspaceId, userId);
        if (optional.isEmpty()) {
            return ApiResult.bindException("工作区不存在");
        }
        final Specification<Note> specification = (root, query, cb) -> {
            final Predicate predicate = cb.and(
                cb.equal(root.get("workspace"), optional.get()),
                cb.equal(root.get("status"), Note.Status.NORMAL)
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
            "获取工作区笔记列表成功"
        );
    }

    @GetMapping("/archived")
    @ApiOperation("获取归档笔记列表")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> archived(
        @UserId final Long userId,
        final Pageable page,
        @RequestParam(value = "search", required = false) final String search
    ) {
        final Set<Workspace> workspaces = workspaceRepository.findByUser(userId);
        final Specification<Note> specification = (root, query, cb) -> {
            final Predicate predicate = cb.and(
                root.get("workspace").in(workspaces),
                cb.equal(root.get("status"), Note.Status.ARCHIVE)
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
            "获取归档笔记列表成功"
        );
    }

    @GetMapping("/deleted")
    @ApiOperation("获取已删除笔记列表")
    @PreAuthorize("hasAuthority('NOTE')")
    public ApiResult<?> deleted(
        @UserId final Long userId,
        final Pageable page,
        @RequestParam(value = "search", required = false) final String search
    ) {
        final Set<Workspace> workspaces = workspaceRepository.findByUser(userId);
        final Specification<Note> specification = (root, query, cb) -> {
            final Predicate predicate = cb.and(
                root.get("workspace").in(workspaces),
                cb.equal(root.get("status"), Note.Status.DELETED)
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
            "获取已删除笔记列表成功"
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
        final Optional<Note> optional = noteRepository.findById(id);
        if (optional.isEmpty() || !userId.equals(optional.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note note = optional.get();
        final Set<Note> notes = note.allChildren();
        notes.add(note);
        notes.forEach(n -> n.setStatus(Note.Status.DELETED));
        noteRepository.saveAll(notes);
        return ApiResult.ok("删除笔记成功").build();
    }

    @DeleteMapping("/{id}/archive")
    @ApiOperation("归档笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> archive(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> optional = noteRepository.findById(id);
        if (optional.isEmpty() || !userId.equals(optional.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note note = optional.get();
        final Set<Note> notes = note.allChildren();
        notes.add(note);
        notes.forEach(n -> n.setStatus(Note.Status.ARCHIVE));
        noteRepository.saveAll(notes);
        return ApiResult.ok("归档笔记成功").build();
    }

    @PutMapping("/{id}/restore")
    @ApiOperation("还原笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> restore(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> optional = noteRepository.findById(id);
        if (optional.isEmpty() || !userId.equals(optional.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note note = optional.get();
        // 如果父节点不是正常挂载状态，则将游离的笔记恢复到工作区根目录内
        final Note parent = note.getParent();
        if (parent != null && parent.getStatus() != Note.Status.NORMAL) {
            note.setParent(null);
        }
        final Set<Note> notes = note.allChildren();
        notes.add(note);
        notes.forEach(n -> n.setStatus(Note.Status.NORMAL));
        noteRepository.saveAll(notes);
        return ApiResult.ok("还原笔记成功").build();
    }

    @DeleteMapping("/{id}/force")
    @ApiOperation("删除笔记（物理删除）")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> deleteForce(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> note = noteRepository.findById(id);
        if (note.isEmpty() || !userId.equals(note.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        noteRepository.deleteById(note.get().getId());
        return ApiResult.ok("删除笔记成功").build();
    }

    @PutMapping("/{id}/share")
    @ApiOperation("分享笔记")
    @PreAuthorize("hasAuthority('NOTE')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> share(@UserId final Long userId, @PathVariable("id") final String id) {
        final Optional<Note> optional = noteRepository.findById(id);
        if (optional.isEmpty() || !userId.equals(optional.get().getWorkspace().getUser())) {
            return ApiResult.bindException("笔记不存在");
        }
        final Note note = optional.get();
        final Boolean share = !note.getShare();
        final Set<Note> notes = note.allChildren();
        notes.add(note);
        notes.forEach(n -> n.setShare(share));
        noteRepository.saveAll(notes);
        return ApiResult.ok("分享/取消分享笔记成功").build();
    }
}
