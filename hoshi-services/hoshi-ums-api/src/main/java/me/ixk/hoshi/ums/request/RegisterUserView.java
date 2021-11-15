/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("注册用户")
public class RegisterUserView {

    @NotNull(message = "验证码不能为空")
    @Size(min = 10, max = 10, message = "验证码的长度为 10 位")
    @ApiModelProperty("验证码")
    private String code;

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    @Size(min = 8, max = 50, message = "密码长度应在（8-50）之间")
    @NotNull(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @NotNull(message = "昵称不能为空")
    @ApiModelProperty("昵称")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式有误")
    @ApiModelProperty("邮箱")
    private String email;
}