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

/**
 * @author Otstar Lin
 * @date 2021/11/23 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("修改笔记")
public class UpdateNoteView {

    @NotNull(message = "修改笔记的时候必须设置笔记 ID")
    @ApiModelProperty("笔记 ID")
    private String id;

    @ApiModelProperty("父笔记 ID")
    private String parent;

    @ApiModelProperty("工作区 ID")
    private String workspace;

    @ApiModelProperty("笔记状态")
    private String status;

    // ==== 以下四项修改会生成历史记录

    @Size(min = 1, max = 255, message = "笔记名称的长度应在（1-255）之间")
    @ApiModelProperty("笔记名称")
    private String name;

    @ApiModelProperty("笔记类型")
    private String content;

    @ApiModelProperty("笔记图标")
    private String icon;

    @ApiModelProperty("属性")
    private String attributes;
}
