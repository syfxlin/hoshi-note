/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.api.view.response.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/5/24 21:29
 */
@Data
@Builder
@ApiModel("文件信息")
public class FileView {

    @ApiModelProperty("文件名")
    private final String fileName;

    @ApiModelProperty("文件大小")
    private final long size;

    @ApiModelProperty("文件 URL")
    private final String url;
}
