/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.NoteConfig;

/**
 * @author Otstar Lin
 * @date 2021/5/31 11:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNoteView {

    private String parent;

    @NotNull(message = "工作区 ID 必须不为空")
    private String workspace;

    @NotNull(message = "工作区的名称必须不为空")
    private String name;

    private String content;

    @Size(min = 1, max = 50, message = "笔记类型的长度应在 1-50 之间")
    @NotNull(message = "笔记类型必须不为空")
    private String type;

    public Note toEntity() {
        final NoteConfig config = new NoteConfig();
        final Note note = Note
            .builder()
            .id(Note.generateId())
            .name(this.getName())
            .content(this.getContent())
            .version(1L)
            .type(this.getType())
            .status(1)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .config(config)
            .build();
        config.setNote(note);
        return note;
    }
}
