/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import lombok.experimental.Accessors;

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
    private LocalDateTime createdTime;

    @ApiModelProperty("修改时间")
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    @JsonBackReference
    @ApiModelProperty("笔记历史")
    @OneToMany(mappedBy = "note")
    @Exclude
    private List<NoteHistory> histories = new ArrayList<>();

    @ApiModelProperty("笔记配置")
    @OneToOne(mappedBy = "note")
    private NoteConfig config;
}
