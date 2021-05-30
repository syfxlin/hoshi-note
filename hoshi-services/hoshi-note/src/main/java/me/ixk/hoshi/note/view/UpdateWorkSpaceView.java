/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.note.entity.WorkSpace;

/**
 * @author Otstar Lin
 * @date 2021/5/30 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkSpaceView {

    @NotNull(message = "更新工作区的时候必须设置工作区 ID")
    private String id;

    @Size(min = 1, max = 50, message = "工作区的名称长度应在 1-50 之间")
    private String name;

    private String description;

    @Size(max = 50, message = "工作区域名的长度应在 50 之类")
    private String domain;

    private String icon;

    public WorkSpace toEntity() {
        final WorkSpace workspace = new WorkSpace();
        workspace.setId(this.getId());
        workspace.setName(this.getName());
        workspace.setDescription(this.getDescription());
        workspace.setDomain(this.getDomain());
        workspace.setIcon(this.getIcon());
        return workspace;
    }
}
