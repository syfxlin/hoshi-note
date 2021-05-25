package me.ixk.hoshi.db.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
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
@EqualsAndHashCode(of = { "id" })
public class NoteHistory {

    @Id
    @ApiModelProperty("历史 ID")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ApiModelProperty("笔记 ID")
    @ManyToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", nullable = false)
    private Note note;

    @ApiModelProperty("保存时间")
    @Column(name = "save_time", nullable = false)
    private LocalDateTime saveTime;

    @ApiModelProperty("版本号")
    @Column(name = "version", columnDefinition = "BIGINT", nullable = false)
    private Long version;

    @ApiModelProperty("标记")
    @Column(name = "tag", length = 50)
    private String tag;
}
