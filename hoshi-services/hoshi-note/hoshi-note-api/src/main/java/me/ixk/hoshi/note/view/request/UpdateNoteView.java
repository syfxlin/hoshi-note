/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.view.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String parent;

    @NotNull(message = "更新笔记时必须设置工作区 ID")
    private String workspace;

    private String name;
    private String content;
    private String type;
    private Integer status;
}
