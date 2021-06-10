/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

/**
 * @author Otstar Lin
 * @date 2021/5/22 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("更新用户信息")
public class UpdateUserInfoView {

    @URL(message = "用户头像地址有误")
    @Size(max = 255, message = "用户头像链接的长度不能超过 255")
    @ApiModelProperty("头像")
    private String avatar;

    @Size(max = 255, message = "用户简介的长度不能超过 255")
    @ApiModelProperty("简介")
    private String bio;

    @Size(max = 255, message = "用户地址的长度不能超过 255")
    @ApiModelProperty("地址")
    private String address;

    @Size(max = 255, message = "用户链接的长度不能超过 255")
    @ApiModelProperty("链接")
    private String url;

    @Size(max = 255, message = "公司名称的长度不能超过 255")
    @ApiModelProperty("公司")
    private String company;

    @Size(max = 255, message = "用户状态的长度不能超过 255")
    @ApiModelProperty("状态")
    private String status;
}
