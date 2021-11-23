/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("笔记")
public class AddNoteView {

    @NotNull(message = "空间 ID 必须不为空")
    @ApiModelProperty("空间")
    private String workspaceId;

    @ApiModelProperty("父笔记")
    private String parentId;

    @NotNull(message = "笔记名称不能为空")
    @Size(min = 1, max = 255, message = "笔记名称的长度应在 1-255 之内")
    @ApiModelProperty("笔记名称")
    private String name;

    @ApiModelProperty("笔记图标")
    private String icon;
}
