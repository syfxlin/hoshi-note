/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/5/27 23:08
 */
@Data
@Builder
@ApiModel("已登录用户信息")
public class LoggedView {

    @ApiModelProperty("Session ID")
    private final String sessionId;

    @ApiModelProperty("用户地址（IP）")
    private final String address;

    @ApiModelProperty("UA 信息")
    private final String userAgent;

    @ApiModelProperty("登录时间")
    private final OffsetDateTime creationTime;

    @ApiModelProperty("最后访问时间")
    private final OffsetDateTime lastAccessedTime;
}
