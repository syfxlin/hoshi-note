/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.mysql.generator.ObjectIdGenerator;
import me.ixk.hoshi.note.request.AddNoteView;
import me.ixk.hoshi.note.request.UpdateNoteView;
import me.ixk.hoshi.note.response.BreadcrumbView;
import me.ixk.hoshi.note.response.BreadcrumbView.Item;
import me.ixk.hoshi.note.response.ListNoteView;
import me.ixk.hoshi.note.response.NoteView;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@Schema(name = "笔记表")
@Table(
    name = "note",
    indexes = {
        @Index(name = "idx_note_name", columnList = "name"), @Index(name = "idx_note_status", columnList = "status"),
    }
)
@Entity
public class Note {

    @Id
    @Schema(name = "空间 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(generator = "objectId")
    @GenericGenerator(name = "objectId", strategy = ObjectIdGenerator.STRATEGY_NAME)
    private String id;

    @JsonBackReference
    @ToString.Exclude
    @Schema(name = "父笔记")
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Note parent;

    @JsonBackReference
    @ToString.Exclude
    @Schema(name = "子笔记")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private Set<Note> children;

    @JsonBackReference
    @ToString.Exclude
    @Schema(name = "空间")
    @ManyToOne
    @JoinColumn(name = "workspace_id", referencedColumnName = "id", nullable = false)
    private Workspace workspace;

    @Schema(name = "笔记名称")
    @Column(name = "name", nullable = false)
    private String name;

    @Schema(name = "笔记内容")
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Schema(name = "笔记图标")
    @Column(name = "icon")
    private String icon;

    @Schema(name = "属性")
    @Column(name = "attributes", columnDefinition = "TEXT")
    private String attributes;

    @Schema(name = "笔记版本号")
    @Column(name = "version", nullable = false)
    private Long version;

    @Schema(name = "笔记状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Schema(name = "创建时间")
    @Column(name = "created_time", nullable = false)
    private OffsetDateTime createdTime;

    @Schema(name = "修改时间")
    @Column(name = "updated_time", nullable = false)
    private OffsetDateTime updatedTime;

    @Schema(name = "分享")
    @Column(name = "share", nullable = false)
    private Boolean share;

    public Set<Note> allChildren() {
        final Set<Note> children = this.getChildren();
        return Stream
            .concat(children.stream(), children.stream().flatMap(note -> note.allChildren().stream()))
            .collect(Collectors.toSet());
    }

    public enum Status {
        /**
         * 正常使用
         */
        NORMAL,
        /**
         * 归档
         */
        ARCHIVE,
        /**
         * 回收站（已删除）
         */
        DELETED,
    }

    public static Note ofAdd(final AddNoteView vo) {
        return Note
            .builder()
            .name(vo.getName())
            .icon(vo.getIcon())
            .version(1L)
            .status(Status.NORMAL)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .share(false)
            .build();
    }

    public static Note ofUpdate(final UpdateNoteView vo) {
        final String status = vo.getStatus();
        return Note
            .builder()
            .id(vo.getId())
            .name(vo.getName())
            .content(vo.getContent())
            .icon(vo.getIcon())
            .status(status == null ? null : Status.valueOf(status))
            .attributes(vo.getAttributes())
            .updatedTime(OffsetDateTime.now())
            .share(vo.getShare())
            .build();
    }

    public ListNoteView toListView() {
        final LinkedList<Item> parent = new LinkedList<>();
        Note curr = this.parent;
        while (curr != null) {
            parent.addFirst(Item.builder().id(curr.getId()).name(curr.getName()).build());
            curr = curr.getParent();
        }
        final BreadcrumbView breadcrumb = BreadcrumbView
            .builder()
            .workspace(Item.builder().id(this.workspace.getId()).name(this.workspace.getName()).build())
            .parent(parent)
            .build();
        return ListNoteView
            .builder()
            .id(this.id)
            .parent(this.parent != null ? this.parent.getId() : null)
            .workspace(this.workspace.getId())
            .name(this.name)
            .icon(this.icon)
            .status(this.status.name())
            .createdTime(this.createdTime)
            .updatedTime(this.updatedTime)
            .share(this.share)
            .breadcrumb(breadcrumb)
            .build();
    }

    public NoteView toView() {
        final LinkedList<Item> parent = new LinkedList<>();
        Note curr = this.parent;
        while (curr != null) {
            parent.addFirst(Item.builder().id(curr.getId()).name(curr.getName()).build());
            curr = curr.getParent();
        }
        final BreadcrumbView breadcrumb = BreadcrumbView
            .builder()
            .workspace(Item.builder().id(this.workspace.getId()).name(this.workspace.getName()).build())
            .parent(parent)
            .build();
        return NoteView
            .builder()
            .id(this.id)
            .parent(this.parent != null ? this.parent.getId() : null)
            .workspace(this.workspace.getId())
            .name(this.name)
            .content(this.content)
            .icon(this.icon)
            .version(this.version)
            .status(this.status.name())
            .attributes(this.attributes)
            .createdTime(this.createdTime)
            .updatedTime(this.updatedTime)
            .share(this.share)
            .breadcrumb(breadcrumb)
            .build();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        final Note note = (Note) o;
        return id != null && Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
