/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import lombok.experimental.Accessors;
import me.ixk.hoshi.api.view.request.note.AddNoteView;
import me.ixk.hoshi.api.view.request.note.UpdateNoteView;
import me.ixk.hoshi.api.view.response.note.NoteView;

/**
 * @author Otstar Lin
 * @date 2021/5/25 16:33
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("笔记表")
@Accessors(chain = true)
@Table(name = "note")
public class Note {

    @Id
    @ApiModelProperty("笔记 ID")
    @Column(name = "id", nullable = false, unique = true, length = 20)
    @Include
    private String id;

    @JsonBackReference
    @ApiModelProperty("父笔记")
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @Exclude
    private Note parent;

    @ApiModelProperty("子笔记")
    @OneToMany(mappedBy = "parent")
    @Exclude
    public List<Note> children = new ArrayList<>();

    @JsonBackReference
    @ApiModelProperty("空间")
    @ManyToOne
    @JoinColumn(name = "workspace_id", referencedColumnName = "id", nullable = false)
    private WorkSpace workspace;

    @ApiModelProperty("笔记名称")
    @Column(name = "name", nullable = false)
    private String name;

    @ApiModelProperty("笔记内容")
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @ApiModelProperty("笔记版本号")
    @Column(name = "version", columnDefinition = "BIGINT", nullable = false)
    private Long version;

    @ApiModelProperty("笔记类型")
    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @ApiModelProperty("笔记状态")
    @Column(name = "status", columnDefinition = "TINYINT")
    private Integer status;

    @ApiModelProperty("创建时间")
    @Column(name = "created_time", nullable = false)
    private OffsetDateTime createdTime;

    @ApiModelProperty("修改时间")
    @Column(name = "updated_time", nullable = false)
    private OffsetDateTime updatedTime;

    @JsonBackReference
    @ApiModelProperty("笔记历史")
    @OneToMany(mappedBy = "note")
    @Exclude
    private List<NoteHistory> histories = new ArrayList<>();

    @ApiModelProperty("笔记配置")
    @OneToOne(mappedBy = "note", cascade = CascadeType.ALL)
    private NoteConfig config;

    public NoteView toView() {
        final Note parent = this.getParent();
        final List<Note> children = this.getChildren();
        return NoteView
            .builder()
            .id(this.getId())
            .parent(parent == null ? null : parent.getId())
            .children(children == null ? null : children.stream().map(Note::getId).collect(Collectors.toList()))
            .workspace(this.getWorkspace().getId())
            .name(this.getName())
            .content(this.getContent())
            .version(this.getVersion())
            .type(this.getType())
            .status(this.getStatus())
            .createdTime(this.getCreatedTime())
            .updatedTime(this.getUpdatedTime())
            .config(this.getConfig().toView())
            .build();
    }

    public static String generateId() {
        return RandomUtil.randomString(10);
    }

    public static Note ofAdd(final AddNoteView vo) {
        final NoteConfig config = new NoteConfig();
        final Note note = Note
            .builder()
            .id(Note.generateId())
            .name(vo.getName())
            .content(vo.getContent())
            .version(1L)
            .type(vo.getType())
            .status(1)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .config(config)
            .build();
        config.setNote(note);
        return note;
    }

    public static Note ofUpdate(final UpdateNoteView vo) {
        return Note
            .builder()
            .id(vo.getId())
            .name(vo.getName())
            .content(vo.getContent())
            .type(vo.getType())
            .status(vo.getStatus())
            .updatedTime(OffsetDateTime.now())
            .build();
    }
}
