/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新文件信息
 *
 * @author Otstar Lin
 * @date 2021/11/16 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "更新文件信息")
public class UpdateFile {

    @NotNull(message = "更新时必须设置文件 id")
    @Schema(name = "文件 ID")
    private Long fileId;

    @Size(min = 1, max = 255, message = "角色名称的长度应在 1-255 之内")
    @Schema(name = "文件名")
    private String name;

    @Schema(name = "文件描述")
    private String description;
}
