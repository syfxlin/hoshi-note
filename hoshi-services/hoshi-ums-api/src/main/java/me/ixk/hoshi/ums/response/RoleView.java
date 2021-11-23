/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@ApiModel("权限")
public class RoleView {

    @ApiModelProperty("角色名称，必须是大写英文")
    private final String name;

    @ApiModelProperty("创建时间")
    private final OffsetDateTime createdTime;

    @ApiModelProperty("状态")
    private final Boolean status;

    @ApiModelProperty("角色的描述")
    private final String description;

    @ApiModelProperty("权限列表")
    private final Set<String> permissions;
}
