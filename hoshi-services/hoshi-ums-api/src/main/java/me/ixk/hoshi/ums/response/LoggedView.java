/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author Otstar Lin
 * @date 2021/5/27 23:08
 */
@Data
@Builder
@Schema(name = "已登录用户信息")
public class LoggedView {

    @Schema(name = "Session ID")
    private final String sessionId;

    @Schema(name = "用户地址（IP）")
    private final String address;

    @Schema(name = "UA 信息")
    private final String userAgent;

    @Schema(name = "登录时间")
    private final OffsetDateTime creationTime;

    @Schema(name = "最后访问时间")
    private final OffsetDateTime lastAccessedTime;

    @Schema(name = "是否是当前访问的设备")
    private final Boolean current;
}
