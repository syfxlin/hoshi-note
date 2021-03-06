/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.db.generator.ObjectIdGenerator;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * 笔记历史
 *
 * @author Otstar Lin
 * @date 2021/11/18 14:57
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Schema(name = "笔记历史表")
@Table(name = "note_history")
@Entity
public class NoteHistory {

    public static final long MIN_UPDATED = 5L;

    @Id
    @Schema(name = "笔记历史 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(generator = "objectId")
    @GenericGenerator(name = "objectId", strategy = ObjectIdGenerator.STRATEGY_NAME)
    private String id;

    @JsonBackReference
    @ToString.Exclude
    @Schema(name = "笔记")
    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", nullable = false)
    private Note note;

    @Schema(name = "保存时间")
    @Column(name = "save_time", nullable = false)
    private OffsetDateTime saveTime;

    @Schema(name = "版本号")
    @Column(name = "version", columnDefinition = "BIGINT", nullable = false)
    private Long version;

    @Schema(name = "笔记名称")
    @Column(name = "name", nullable = false)
    private String name;

    @Schema(name = "笔记内容")
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Schema(name = "笔记图标")
    @Column(name = "icon")
    private String icon;

    @Schema(name = "属性")
    @Column(name = "attributes", columnDefinition = "TEXT")
    private String attributes;

    public static boolean needSave(Note update, Note origin) {
        OffsetDateTime lastTime = origin.getUpdatedTime();
        OffsetDateTime nowTime = update.getUpdatedTime();
        if (lastTime == null || nowTime == null || lastTime.isBefore(nowTime.minusMinutes(NoteHistory.MIN_UPDATED))) {
            return false;
        }
        String name = update.getName();
        String content = update.getContent();
        String icon = update.getIcon();
        String attributes = update.getAttributes();
        return (
            (name != null && !name.equals(origin.getName())) ||
                (content != null && !content.equals(origin.getContent())) ||
                (icon != null && !icon.equals(origin.getIcon())) ||
                (attributes != null && !attributes.equals(origin.getAttributes()))
        );
    }

    public static NoteHistory of(Note note) {
        return NoteHistory
            .builder()
            .note(note)
            .saveTime(note.getUpdatedTime())
            .version(note.getVersion())
            .name(note.getName())
            .content(note.getContent())
            .icon(note.getIcon())
            .attributes(note.getAttributes())
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
        NoteHistory that = (NoteHistory) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
