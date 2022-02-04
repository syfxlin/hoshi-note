/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 文件
 *
 * @author Otstar Lin
 * @date 2021/11/14 22:34
 */
@Data
@Builder
@Schema(name = "文件信息")
public class FileView {

    @Schema(name = "文件 ID")
    private final Long id;

    @Schema(name = "存储文件名")
    private final String disk;

    @Schema(name = "文件名")
    private final String name;

    @Schema(name = "文件描述")
    private final String description;

    @Schema(name = "文件大小")
    private final Long size;

    @Schema(name = "文件类型")
    private final String contentType;

    @Schema(name = "上传时间")
    private final OffsetDateTime uploadedTime;

    @Schema(name = "文件 URL")
    private final String url;
}
