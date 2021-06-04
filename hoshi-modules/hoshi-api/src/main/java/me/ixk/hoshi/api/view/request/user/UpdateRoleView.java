/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.view.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("更新权限")
public class UpdateRoleView {

    @Size(min = 1, max = 50, message = "权限名称的长度应在 1-50 之内")
    @Pattern(regexp = "[A-Z]+", message = "权限名称必须是全大写的英文字符")
    @ApiModelProperty("权限名称")
    private String roleName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态")
    private Boolean status;
}
