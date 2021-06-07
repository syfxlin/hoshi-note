/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.log.view.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/6/7 21:36
 */
@Data
@Builder
@ApiModel("日志")
public class LogView {

    @ApiModelProperty("日志类型")
    private String type;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("IP 地址")
    private String ip;

    @ApiModelProperty("用户")
    private String user;

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("操作名称")
    private String operate;

    @ApiModelProperty("操作起始时间")
    private OffsetDateTime startTime;

    @ApiModelProperty("操作结束时间")
    private OffsetDateTime endTime;

    @ApiModelProperty("操作状态")
    private Boolean status;

    @ApiModelProperty("操作信息")
    private String message;

    @ApiModelProperty("创建时间")
    private OffsetDateTime createdTime;
}
