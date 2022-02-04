/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Otstar Lin
 * @date 2021/5/20 15:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "编辑用户角色")
public class EditUserRoleView {

    @NotNull(message = "修改角色时必须设置用户 id")
    @Schema(name = "用户 ID")
    private Long userId;

    @Schema(name = "角色列表")
    private Set<String> roles = new HashSet<>();
}
