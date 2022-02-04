/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.file.request.UpdateFile;
import me.ixk.hoshi.file.response.FileView;

import javax.persistence.*;
import java.time.OffsetDateTime;

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
@Schema(name = "用户表")
@Entity
@Table(
    name = "file",
    indexes = {
        @Index(name = "idx_file_user_id_unq", columnList = "user_id", unique = true),
        @Index(name = "idx_file_disk_unq", columnList = "disk", unique = true),
        @Index(name = "idx_file_name_unq", columnList = "name", unique = true),
    }
)
public class File {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "文件 ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(name = "用户 ID")
    private Long user;

    @Column(name = "disk", nullable = false, unique = true)
    @Schema(name = "存储文件名")
    private String disk;

    @Column(name = "name", nullable = false)
    @Schema(name = "文件名称")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(name = "文件描述")
    private String description;

    @Column(name = "size", nullable = false)
    @Schema(name = "文件大小")
    private Long size;

    @Column(name = "content_type")
    @Schema(name = "文件类型")
    private String contentType;

    @Column(name = "uploaded_time", nullable = false)
    @Schema(name = "上传时间")
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
            .url(String.format("/files/%s/%s", this.user, this.disk))
            .build();
    }

    public String toPath() {
        return String.format("%s/%s", this.getUser(), this.getDisk());
    }
}
