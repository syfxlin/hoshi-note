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
 * @date 2021/11/19 19:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "修改工作区")
public class UpdateWorkspaceView {

    @NotNull(message = "修改工作区的时候必须设置工作区 ID")
    @Schema(name = "空间 ID")
    private String id;

    @Size(min = 1, max = 255, message = "工作区名称的长度应在（1-255）之间")
    @Schema(name = "空间名称")
    private String name;

    @Schema(name = "空间描述")
    private String description;

    @Schema(name = "域名")
    private String domain;

    @Schema(name = "空间图标")
    private String icon;

    @Schema(name = "是否公开")
    private Boolean disclose;
}
