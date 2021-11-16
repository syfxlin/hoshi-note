/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.file.request.UpdateFile;
import me.ixk.hoshi.file.response.FileView;

/**
 * 文件
 *
 * @author Otstar Lin
 * @date 2021/11/15 19:50
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel("用户表")
@Entity
@Table(name = "file")
public class File {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("文件 ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty("用户 ID")
    private Long userId;

    @Column(name = "disk", nullable = false, unique = true)
    @ApiModelProperty("存储文件名")
    private String disk;

    @Column(name = "name", nullable = false)
    @ApiModelProperty("文件名称")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @ApiModelProperty("文件描述")
    private String description;

    @Column(name = "size", nullable = false)
    @ApiModelProperty("文件大小")
    private Long size;

    @Column(name = "content_type")
    @ApiModelProperty("文件类型")
    private String contentType;

    @Column(name = "uploaded_time", nullable = false)
    @ApiModelProperty("上传时间")
    private OffsetDateTime uploadedTime;

    public static File ofUpdate(final UpdateFile file) {
        return File.builder().id(file.getFileId()).name(file.getName()).description(file.getDescription()).build();
    }

    public FileView toView() {
        return FileView
            .builder()
            .id(this.id)
            .disk(this.disk)
            .name(this.name)
            .description(this.description)
            .size(this.size)
            .contentType(this.contentType)
            .uploadedTime(this.uploadedTime)
            .url(String.format("/files/%s/%s", this.userId, this.disk))
            .build();
    }

    public String toPath() {
        return String.format("%s/%s", this.getUserId(), this.getDisk());
    }
}
