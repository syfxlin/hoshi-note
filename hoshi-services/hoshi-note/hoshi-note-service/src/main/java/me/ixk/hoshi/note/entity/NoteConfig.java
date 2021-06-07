/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import lombok.experimental.Accessors;
import me.ixk.hoshi.note.view.response.NoteConfigView;

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
public class NoteConfig {

    @Id
    @ApiModelProperty("配置 ID")
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private Long id;

    @JsonBackReference
    @ApiModelProperty("笔记 ID")
    @OneToOne
    @JoinColumn(name = "note_id", referencedColumnName = "id", nullable = false)
    @Exclude
    private Note note;

    @ApiModelProperty("笔记范围密码")
    @Column(name = "password")
    private String password;

    public NoteConfigView toView() {
        return NoteConfigView.builder().id(this.getId()).password(this.getPassword()).build();
    }
}
