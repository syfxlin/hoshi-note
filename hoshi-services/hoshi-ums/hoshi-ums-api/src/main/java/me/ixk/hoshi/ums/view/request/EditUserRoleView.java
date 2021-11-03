/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/20 15:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("编辑用户角色")
public class EditUserRoleView {

    @NotNull(message = "修改角色时必须设置用户 id")
    @ApiModelProperty("用户 ID")
    private String userId;

    @ApiModelProperty("角色列表")
    private List<String> roles = new ArrayList<>();
}