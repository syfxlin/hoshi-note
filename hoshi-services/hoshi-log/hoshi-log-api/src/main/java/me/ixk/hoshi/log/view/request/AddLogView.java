/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.log.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/6/4 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("添加日志")
public class AddLogView {

    @NotNull(message = "日志类型不能为空")
    @Pattern(regexp = "[A-Z]+", message = "日志类型必须为全大写字符")
    @ApiModelProperty("日志类型")
    private String type;

    @NotNull(message = "操作名称不能为空")
    @ApiModelProperty("操作名称")
    private String operate;

    @NotNull(message = "日志信息不能为空")
    @ApiModelProperty("操作信息")
    private String message;

    @NotNull(message = "模块信息不能为空")
    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("IP 地址")
    private String ip;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("用户")
    private String user;

    @ApiModelProperty("操作起始时间")
    private OffsetDateTime startTime;

    @ApiModelProperty("操作结束时间")
    private OffsetDateTime endTime;

    @NotNull(message = "操作状态不能为空")
    @ApiModelProperty("操作状态")
    private Boolean status;
}
