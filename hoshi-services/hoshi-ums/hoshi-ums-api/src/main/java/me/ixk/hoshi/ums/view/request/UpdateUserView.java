/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 10:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("更新用户")
public class UpdateUserView {

    @NotNull(message = "更新时必须设置用户 id")
    @ApiModelProperty("用户 ID")
    private String userId;

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @ApiModelProperty("用户名")
    private String username;

    @Size(min = 8, max = 50, message = "密码长度应在（3-50）之间")
    @ApiModelProperty("密码")
    private String password;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @ApiModelProperty("昵称")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email(message = "邮箱格式有误")
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("状态")
    private Boolean status;
}
