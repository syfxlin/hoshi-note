/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 12:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "过滤用户查询")
public class FilterUserView {

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @Schema(name = "用户名")
    private String username;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @Schema(name = "昵称")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email(message = "邮箱格式有误")
    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "状态")
    private Boolean status;
}
