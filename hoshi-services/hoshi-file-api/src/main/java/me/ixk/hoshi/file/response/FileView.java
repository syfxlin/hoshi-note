/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 文件
 *
 * @author Otstar Lin
 * @date 2021/11/14 22:34
 */
@Data
@Builder
@ApiModel("文件信息")
public class FileView {

    @ApiModelProperty("文件 ID")
    private Long id;

    @ApiModelProperty("存储文件名")
    private String disk;

    @ApiModelProperty("文件名")
    private final String name;

    @ApiModelProperty("文件描述")
    private String description;

    @ApiModelProperty("文件大小")
    private final Long size;

    @ApiModelProperty("上传时间")
    private OffsetDateTime uploadedTime;

    @ApiModelProperty("文件 URL")
    private final String url;
}
