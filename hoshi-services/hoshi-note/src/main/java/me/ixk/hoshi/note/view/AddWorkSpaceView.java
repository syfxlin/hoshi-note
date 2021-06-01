/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import java.time.OffsetDateTime;
import java.util.Collections;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.note.entity.WorkSpace;

/**
 * @author Otstar Lin
 * @date 2021/5/29 17:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWorkSpaceView {

    @Size(min = 1, max = 50, message = "工作区的名称长度应在 1-50 之间")
    @NotNull(message = "工作区的名称必须不为空")
    private String name;

    private String description;

    @Size(max = 50, message = "工作区域名的长度应在 50 之类")
    private String domain;

    private String icon;

    public WorkSpace toEntity() {
        final WorkSpace workspace = new WorkSpace();
        workspace.setId(WorkSpace.generateId());
        workspace.setName(this.getName());
        workspace.setDescription(this.getDescription());
        workspace.setDomain(this.getDomain());
        workspace.setIcon(this.getIcon());
        workspace.setCreatedTime(OffsetDateTime.now());
        workspace.setNotes(Collections.emptyList());
        return workspace;
    }
}