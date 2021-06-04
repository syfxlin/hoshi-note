/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import me.ixk.hoshi.common.json.JsonMode;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.NoteConfig;

/**
 * @author Otstar Lin
 * @date 2021/5/30 20:54
 */
@Data
@Builder
public class NoteView {

    private final String id;
    private final String parent;
    private final List<String> children;
    private final String workspace;
    private final String name;

    @JsonMode(NoContent.class)
    private final String content;

    private final Long version;
    private final String type;
    private final Integer status;
    private final OffsetDateTime createdTime;
    private final OffsetDateTime updatedTime;
    private final NoteConfig config;

    public static NoteView of(final Note note) {
        final Note parent = note.getParent();
        final List<Note> children = note.getChildren();
        return NoteView
            .builder()
            .id(note.getId())
            .parent(parent == null ? null : parent.getId())
            .children(children == null ? null : children.stream().map(Note::getId).collect(Collectors.toList()))
            .workspace(note.getWorkspace().getId())
            .name(note.getName())
            .content(note.getContent())
            .version(note.getVersion())
            .type(note.getType())
            .status(note.getStatus())
            .createdTime(note.getCreatedTime())
            .updatedTime(note.getUpdatedTime())
            .config(note.getConfig())
            .build();
    }

    public static class NoContent {}
}
