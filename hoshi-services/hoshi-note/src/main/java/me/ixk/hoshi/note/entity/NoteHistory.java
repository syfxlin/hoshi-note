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
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

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
@ApiModel("笔记历史表")
@Table(name = "note_history", indexes = { @Index(name = "idx_notehistory_tag", columnList = "tag") })
@Entity
public class NoteHistory {

    @Id
    @ApiModelProperty("笔记历史 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(generator = "objectId")
    @GenericGenerator(name = "objectId", strategy = ObjectIdGenerator.STRATEGY_NAME)
    private String id;

    @JsonBackReference
    @ToString.Exclude
    @ApiModelProperty("笔记")
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
