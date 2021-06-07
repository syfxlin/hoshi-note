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
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/20 15:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("编辑用户权限")
public class EditUserRoleView {

    @NotNull(message = "修改权限时必须设置用户 id")
    @ApiModelProperty("用户 ID")
    private String userId;

    @ApiModelProperty("权限列表")
    private List<String> roles = new ArrayList<>();
}
