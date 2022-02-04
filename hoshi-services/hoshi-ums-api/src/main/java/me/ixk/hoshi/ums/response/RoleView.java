/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@Schema(name = "权限")
public class RoleView {

    @Schema(name = "角色名称，必须是大写英文")
    private final String name;

    @Schema(name = "创建时间")
    private final OffsetDateTime createdTime;

    @Schema(name = "状态")
    private final Boolean status;

    @Schema(name = "角色的描述")
    private final String description;

    @Schema(name = "权限列表")
    private final Set<String> permissions;
}
