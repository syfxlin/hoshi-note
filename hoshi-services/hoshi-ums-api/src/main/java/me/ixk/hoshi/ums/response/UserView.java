/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@ApiModel("用户")
public class UserView {

    @ApiModelProperty("用户 ID")
    private final Long id;

    @ApiModelProperty("用户名")
    private final String username;

    @ApiModelProperty("昵称")
    private final String nickname;

    @ApiModelProperty("邮箱")
    private final String email;

    @ApiModelProperty("用户状态")
    private final Boolean status;

    @ApiModelProperty("创建时间")
    private final OffsetDateTime createdTime;

    @ApiModelProperty("用户角色列表")
    private final Set<RoleView> roles;

    @ApiModelProperty("用户信息")
    private final UserInfoView info;

    @ApiModelProperty("用户关注了数量")
    private final Integer followingCount;

    @ApiModelProperty("用户关注者数量")
    private final Integer followersCount;
}
