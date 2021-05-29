/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import java.time.LocalDateTime;
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

    private String name;
    private String description;
    private String domain;
    private String icon;

    public WorkSpace toEntity() {
        final WorkSpace workspace = new WorkSpace();
        workspace.setId(WorkSpace.generateId());
        workspace.setName(this.getName());
        workspace.setDescription(this.getDescription());
        workspace.setDomain(this.getDomain());
        workspace.setIcon(this.getIcon());
        workspace.setCreatedTime(LocalDateTime.now());
        return workspace;
    }
}
