/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/18 19:40
 */
@Data
@Builder
@ApiModel("笔记")
public class NoteView {

    @ApiModelProperty("空间 ID")
    private final String id;

    @ApiModelProperty("父笔记")
    private final String parent;

    @ApiModelProperty("空间")
    private final String workspace;

    @ApiModelProperty("笔记名称")
    private final String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty("笔记内容")
    private final String content;

    @ApiModelProperty("笔记图标")
    private String icon;

    @ApiModelProperty("笔记版本号")
    private final Long version;

    @ApiModelProperty("笔记状态")
    private final String status;

    @ApiModelProperty("属性")
    private final String attributes;

    @ApiModelProperty("创建时间")
    private final OffsetDateTime createdTime;

    @ApiModelProperty("修改时间")
    private final OffsetDateTime updatedTime;

    @ApiModelProperty("面包屑")
    private final BreadcrumbView breadcrumb;
}
