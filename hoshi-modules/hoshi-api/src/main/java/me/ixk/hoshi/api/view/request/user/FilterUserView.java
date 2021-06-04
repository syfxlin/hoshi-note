/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.view.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 12:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("过滤用户查询")
public class FilterUserView {

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @ApiModelProperty("用户名")
    private String username;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @ApiModelProperty("昵称")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email(message = "邮箱格式有误")
    @ApiModelProperty("邮箱")
    private String email;

    @Min(value = 0, message = "状态值最小不能小于 0")
    @Max(value = 127, message = "状态值最大不能超过 127")
    @ApiModelProperty("状态")
    private Integer status;
}
