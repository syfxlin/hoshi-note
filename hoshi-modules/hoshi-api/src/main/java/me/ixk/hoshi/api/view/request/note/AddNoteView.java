/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.view.request.note;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
