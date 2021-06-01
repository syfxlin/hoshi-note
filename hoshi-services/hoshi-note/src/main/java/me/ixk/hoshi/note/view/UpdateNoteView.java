/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.note.entity.Note;

/**
 * @author Otstar Lin
 * @date 2021/5/31 21:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNoteView {

    @NotNull(message = "更新笔记时必须设置 ID")
    private String id;

    private String parentId;

    @NotNull(message = "更新笔记时必须设置工作区 ID")
    private String workspaceId;

    private String name;
    private String content;
    private String type;
    private Integer status;

    public Note toEntity() {
        return Note
            .builder()
            .id(this.getId())
            .name(this.getName())
            .content(this.getContent())
            .type(this.getType())
            .status(this.getStatus())
            .updatedTime(OffsetDateTime.now())
            .build();
    }
}
