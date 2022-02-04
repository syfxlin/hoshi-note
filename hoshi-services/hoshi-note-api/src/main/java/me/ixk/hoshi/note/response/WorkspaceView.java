/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author Otstar Lin
 * @date 2021/11/18 19:22
 */
@Data
@Builder
@Schema(name = "工作区")
public class WorkspaceView {

    @Schema(name = "空间 ID")
    private final String id;

    @Schema(name = "用户")
    private final Long user;

    @Schema(name = "空间名称")
    private final String name;

    @Schema(name = "空间描述")
    private final String description;

    @Schema(name = "域名")
    private final String domain;

    @Schema(name = "空间图标")
    private final String icon;

    @Schema(name = "创建时间")
    private final OffsetDateTime createdTime;

    @Schema(name = "是否公开")
    private final Boolean disclose;
}
