/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.mysql.generator.ObjectIdGenerator;
import me.ixk.hoshi.note.request.AddNoteView;
import me.ixk.hoshi.note.request.UpdateNoteView;
import me.ixk.hoshi.note.response.NoteView;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 笔记
 *
 * @author Otstar Lin
 * @date 2021/11/18 14:27
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel("笔记表")
@Table(
    name = "note",
    indexes = {
        @Index(name = "idx_note_name", columnList = "name"), @Index(name = "idx_note_status", columnList = "status"),
    }
)
@Entity
public class Note {

    @Id
    @ApiModelProperty("空间 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(generator = "objectId")
    @GenericGenerator(name = "objectId", strategy = ObjectIdGenerator.STRATEGY_NAME)
    private String id;

    @JsonBackReference
    @ToString.Exclude
    @ApiModelProperty("父笔记")
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Note parent;

    @JsonBackReference
    @ToString.Exclude
    @ApiModelProperty("空间")
    @ManyToOne
    @JoinColumn(name = "workspace_id", referencedColumnName = "id", nullable = false)
    private Workspace workspace;

    @ApiModelProperty("笔记名称")
    @Column(name = "name", nullable = false)
    private String name;

    @ApiModelProperty("笔记内容")
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @ApiModelProperty("笔记图标")
    @Column(name = "icon")
    private String icon;

    @ApiModelProperty("笔记版本号")
    @Column(name = "version", nullable = false)
    private Long version;

    @ApiModelProperty("笔记状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ApiModelProperty("属性")
    @Column(name = "attributes", columnDefinition = "TEXT")
    private String attributes;

    @ApiModelProperty("创建时间")
    @Column(name = "created_time", nullable = false)
    private OffsetDateTime createdTime;

    @ApiModelProperty("修改时间")
    @Column(name = "updated_time", nullable = false)
    private OffsetDateTime updatedTime;

    public enum Status {
        /**
         * 正常使用
         */
        NORMAL,
        /**
         * 编辑锁定
         */
        LOCKED,
        /**
         * 归档
         */
        ARCHIVE,
        /**
         * 回收站（已删除）
         */
        DELETED,
    }

    public static Note ofAdd(AddNoteView vo) {
        return Note
            .builder()
            .name(vo.getName())
            .icon(vo.getIcon())
            .version(1L)
            .status(Status.NORMAL)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .build();
    }

    public static Note ofUpdate(UpdateNoteView vo) {
        String status = vo.getStatus();
        return Note
            .builder()
            .id(vo.getId())
            .name(vo.getName())
            .content(vo.getContent())
            .icon(vo.getIcon())
            .status(status == null ? null : Status.valueOf(status))
            .attributes(vo.getAttributes())
            .build();
    }

    public NoteView toView(boolean content) {
        return NoteView
            .builder()
            .id(this.id)
            .parent(this.parent != null ? this.parent.getId() : null)
            .workspace(this.workspace.getId())
            .name(this.name)
            .content(content ? this.content : null)
            .icon(this.icon)
            .version(this.version)
            .status(this.status.name())
            .attributes(this.attributes)
            .createdTime(this.createdTime)
            .updatedTime(this.updatedTime)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Note note = (Note) o;
        return id != null && Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
