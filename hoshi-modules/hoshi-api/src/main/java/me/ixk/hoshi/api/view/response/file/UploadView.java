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
 * @date 2021/5/23 10:27
 */
@Data
@Builder
@ApiModel("上传响应")
public class UploadView {

    @ApiModelProperty("扩展名")
    private final String extName;

    @ApiModelProperty("文件类型")
    private final String mediaType;

    @ApiModelProperty("文件名")
    private final String fileName;

    @ApiModelProperty("文件大小")
    private final long size;

    @ApiModelProperty("文件 URL")
    private final String url;
}
