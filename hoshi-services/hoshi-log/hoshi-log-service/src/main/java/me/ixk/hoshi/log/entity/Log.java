/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.log.entity;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.ixk.hoshi.log.view.request.AddLogView;
import me.ixk.hoshi.log.view.response.LogView;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Otstar Lin
 * @date 2021/6/4 20:25
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("log")
@ApiModel("日志")
@Accessors(chain = true)
public class Log {

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

    public LogView toView() {
        final LogView view = LogView.builder().build();
        BeanUtil.copyProperties(this, view);
        return view;
    }

    public static Log ofAdd(final AddLogView vo) {
        return Log
            .builder()
            .type(vo.getType())
            .method(vo.getMethod())
            .ip(vo.getIp())
            .user(vo.getUser())
            .module(vo.getModule())
            .operate(vo.getOperate())
            .startTime(vo.getStartTime())
            .endTime(vo.getEndTime())
            .status(vo.getStatus())
            .message(vo.getMessage())
            .createdTime(OffsetDateTime.now())
            .build();
    }
}
