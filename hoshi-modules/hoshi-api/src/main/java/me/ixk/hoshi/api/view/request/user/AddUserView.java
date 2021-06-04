/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.view.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 9:41
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("添加用户")
public class AddUserView extends RegisterUserView {

    @NotNull(message = "状态值不能为空")
    @ApiModelProperty("状态")
    private Boolean status;

    public AddUserView(
        final String username,
        final String password,
        final String nickname,
        final String email,
        final Boolean status
    ) {
        super(username, password, nickname, email);
        this.status = status;
    }
}
