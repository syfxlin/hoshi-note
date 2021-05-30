/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
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
    private final String workspaceId;
    private final String name;
    private Long version;
    private String type;
    private Integer status;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private NoteConfig config;

    public static NoteView of(final Note note) {
        return NoteView
            .builder()
            .id(note.getId())
            .workspaceId(note.getWorkspace().getId())
            .name(note.getName())
            .version(note.getVersion())
            .type(note.getType())
            .status(note.getStatus())
            .createdTime(note.getCreatedTime())
            .updatedTime(note.getUpdatedTime())
            .config(note.getConfig())
            .build();
    }
}
