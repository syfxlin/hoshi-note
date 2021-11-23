/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 16:10
 */
@Data
@Builder
@ApiModel("用户信息")
public class UserInfoView {

    @ApiModelProperty("用户头像的地址")
    private final String avatar;

    @ApiModelProperty("用户简介")
    private final String bio;

    @ApiModelProperty("用户地址")
    private final String address;

    @ApiModelProperty("用户链接")
    private final String url;

    @ApiModelProperty("公司名称")
    private final String company;

    @ApiModelProperty("用户状态")
    private final String status;
}
