/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Otstar Lin
 * @date 2021/11/23 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "修改笔记")
public class UpdateNoteView {

    @NotNull(message = "修改笔记的时候必须设置笔记 ID")
    @Schema(name = "笔记 ID")
    private String id;

    @Schema(name = "父笔记 ID")
    private String parent;

    @Schema(name = "工作区 ID")
    private String workspace;

    @Schema(name = "笔记状态")
    private String status;

    @Schema(name = "分享")
    private Boolean share;

    // ==== 以下四项修改会生成历史记录

    @Size(min = 1, max = 255, message = "笔记名称的长度应在（1-255）之间")
    @Schema(name = "笔记名称")
    private String name;

    @Schema(name = "笔记类型")
    private String content;

    @Schema(name = "笔记图标")
    private String icon;

    @Schema(name = "属性")
    private String attributes;
}
