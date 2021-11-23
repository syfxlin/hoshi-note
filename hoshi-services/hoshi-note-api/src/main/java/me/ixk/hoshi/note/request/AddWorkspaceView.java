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
 * @date 2021/11/19 19:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("添加工作区")
public class AddWorkspaceView {

    @NotNull(message = "工作区名称不能为空")
    @Size(min = 1, max = 255, message = "工作区名称的长度应在（1-255）之间")
    @ApiModelProperty("空间名称")
    private String name;

    @ApiModelProperty("空间描述")
    private String description;

    @ApiModelProperty("域名")
    private String domain;

    @ApiModelProperty("空间图标")
    private String icon;

    @NotNull(message = "是否公开状态不能为空")
    @ApiModelProperty("是否公开")
    private Boolean disclose;
}
