/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author Otstar Lin
 * @date 2021/11/18 19:40
 */
@Data
@Builder
@Schema(name = "笔记")
public class NoteView {

    @Schema(name = "空间 ID")
    private final String id;

    @Schema(name = "父笔记")
    private final String parent;

    @Schema(name = "空间")
    private final String workspace;

    @Schema(name = "笔记名称")
    private final String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "笔记内容")
    private final String content;

    @Schema(name = "笔记图标")
    private final String icon;

    @Schema(name = "笔记版本号")
    private final Long version;

    @Schema(name = "笔记状态")
    private final String status;

    @Schema(name = "属性")
    private final String attributes;

    @Schema(name = "创建时间")
    private final OffsetDateTime createdTime;

    @Schema(name = "修改时间")
    private final OffsetDateTime updatedTime;

    @Schema(name = "分享")
    private final Boolean share;

    @Schema(name = "面包屑")
    private final BreadcrumbView breadcrumb;
}
