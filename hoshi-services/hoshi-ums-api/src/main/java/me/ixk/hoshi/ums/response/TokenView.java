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
@ApiModel("令牌")
public class TokenView {

    @ApiModelProperty("Token ID")
    private final Long id;

    @ApiModelProperty("Token")
    private final String token;

    @ApiModelProperty("名称")
    private final String name;
}
