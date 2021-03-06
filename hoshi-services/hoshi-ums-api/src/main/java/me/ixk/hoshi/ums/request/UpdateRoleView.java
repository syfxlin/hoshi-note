/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "更新角色")
public class UpdateRoleView {

    @Size(min = 1, max = 50, message = "角色名称的长度应在 1-50 之内")
    @Pattern(regexp = "[A-Z_]+", message = "角色名称必须是全大写的英文字符")
    @Schema(name = "角色名称")
    private String roleName;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "状态")
    private Boolean status;

    @Schema(name = "权限列表")
    private Set<String> permissions;
}
