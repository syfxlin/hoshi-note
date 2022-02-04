/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@Schema(name = "用户")
public class UserView {

    @Schema(name = "用户 ID")
    private final Long id;

    @Schema(name = "用户名")
    private final String username;

    @Schema(name = "昵称")
    private final String nickname;

    @Schema(name = "邮箱")
    private final String email;

    @Schema(name = "用户状态")
    private final Boolean status;

    @Schema(name = "创建时间")
    private final OffsetDateTime createdTime;

    @Schema(name = "用户角色列表")
    private final Set<RoleView> roles;

    @Schema(name = "用户信息")
    private final UserInfoView info;

    @Schema(name = "用户关注了数量")
    private final Integer followingCount;

    @Schema(name = "用户关注者数量")
    private final Integer followersCount;
}
