/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@Schema(name = "用户信息")
public class UserInfoView {

    @Schema(name = "用户头像的地址")
    private final String avatar;

    @Schema(name = "用户简介")
    private final String bio;

    @Schema(name = "用户地址")
    private final String address;

    @Schema(name = "用户链接")
    private final String url;

    @Schema(name = "公司名称")
    private final String company;

    @Schema(name = "用户状态")
    private final String status;
}
