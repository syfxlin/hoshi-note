package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Otstar Lin
 * @date 2021/5/27 17:26
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("笔记配置表")
@Accessors(chain = true)
@Table(name = "note_config")
@EqualsAndHashCode(of = { "id" })
public class NoteConfig {

    @Id
    @ApiModelProperty("配置 ID")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ApiModelProperty("笔记 ID")
    @OneToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", nullable = false)
    private Note note;

    @ApiModelProperty("笔记范围密码")
    @Column(name = "password")
    private String password;
}
