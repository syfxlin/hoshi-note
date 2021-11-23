/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 19:22
 */
@Data
@Builder
@ApiModel("工作区")
public class WorkspaceView {

    @ApiModelProperty("空间 ID")
    private final String id;

    @ApiModelProperty("用户")
    private final Long user;

    @ApiModelProperty("空间名称")
    private final String name;

    @ApiModelProperty("空间描述")
    private final String description;

    @ApiModelProperty("域名")
    private final String domain;

    @ApiModelProperty("空间图标")
    private final String icon;

    @ApiModelProperty("创建时间")
    private final OffsetDateTime createdTime;

    @ApiModelProperty("是否公开")
    private final Boolean disclose;
}
