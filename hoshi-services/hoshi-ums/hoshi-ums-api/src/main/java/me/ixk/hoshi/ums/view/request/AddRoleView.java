/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("添加权限")
public class AddRoleView {

    @NotNull(message = "权限名称不能为空")
    @Size(min = 1, max = 50, message = "权限名称的长度应在 1-50 之内")
    @Pattern(regexp = "[A-Z_]+", message = "权限名称必须是全大写的英文字符")
    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @NotNull(message = "状态值不能为空")
    @ApiModelProperty("状态")
    private Boolean status;
}
