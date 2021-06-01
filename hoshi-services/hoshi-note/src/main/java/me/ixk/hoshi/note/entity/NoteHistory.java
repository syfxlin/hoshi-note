/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Otstar Lin
 * @date 2021/5/25 19:42
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("笔记历史表")
@Accessors(chain = true)
@Table(name = "note_history")
public class NoteHistory {

    public static final long MIN_UPDATED = 5L;

    @Id
    @ApiModelProperty("历史 ID")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private Long id;

    @JsonBackReference
    @ApiModelProperty("笔记 ID")
    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", nullable = false)
    private Note note;

    @ApiModelProperty("保存时间")
    @Column(name = "save_time", nullable = false)
    private OffsetDateTime saveTime;

    @ApiModelProperty("版本号")
    @Column(name = "version", columnDefinition = "BIGINT", nullable = false)
    private Long version;

    @ApiModelProperty("标记")
    @Column(name = "tag", length = 50)
    private String tag;

    @ApiModelProperty("笔记内容")
    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    public static NoteHistory of(final Note note) {
        return NoteHistory
            .builder()
            .note(note)
            .saveTime(note.getUpdatedTime())
            .version(note.getVersion())
            .content(note.getContent())
            .build();
    }
}
