/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/11/24 13:50
 */
@Data
@Builder
@ApiModel("笔记")
public class ListNoteView {

    @ApiModelProperty("空间 ID")
    private final String id;

    @ApiModelProperty("父笔记")
    private final String parent;

    @ApiModelProperty("空间")
    private final String workspace;

    @ApiModelProperty("笔记名称")
    private final String name;

    @ApiModelProperty("笔记图标")
    private String icon;

    @ApiModelProperty("笔记状态")
    private final String status;

    @ApiModelProperty("创建时间")
    private final OffsetDateTime createdTime;

    @ApiModelProperty("修改时间")
    private final OffsetDateTime updatedTime;
}
