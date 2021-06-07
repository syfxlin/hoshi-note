/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import lombok.experimental.Accessors;
import me.ixk.hoshi.note.view.request.AddWorkSpaceView;
import me.ixk.hoshi.note.view.request.UpdateWorkSpaceView;

/**
 * @author Otstar Lin
 * @date 2021/5/25 16:25
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("空间表")
@Accessors(chain = true)
@Table(name = "workspace")
public class WorkSpace {

    @Id
    @ApiModelProperty("空间 ID")
    @Column(name = "id", nullable = false, unique = true, length = 20)
    @Include
    private String id;

    @ApiModelProperty("空间名称")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ApiModelProperty("空间描述")
    @Column(name = "description")
    private String description;

    @ApiModelProperty("域名")
    @Column(name = "domain", unique = true, length = 50)
    private String domain;

    @ApiModelProperty("空间图标")
    @Column(name = "icon")
    private String icon;

    @ApiModelProperty("创建时间")
    @Column(name = "created_time", nullable = false)
    private OffsetDateTime createdTime;

    @ApiModelProperty("笔记")
    @OneToMany(mappedBy = "workspace")
    @Exclude
    @JsonBackReference
    private List<Note> notes;

    @ApiModelProperty("用户")
    @Column(name = "user_id")
    private String userId;

    public static String generateId() {
        return RandomUtil.randomString(10);
    }

    public static WorkSpace ofAdd(final AddWorkSpaceView vo) {
        return WorkSpace
            .builder()
            .id(WorkSpace.generateId())
            .name(vo.getName())
            .description(vo.getDescription())
            .domain(vo.getDomain())
            .icon(vo.getIcon())
            .createdTime(OffsetDateTime.now())
            .notes(Collections.emptyList())
            .build();
    }

    public static WorkSpace ofUpdate(final UpdateWorkSpaceView vo) {
        return WorkSpace
            .builder()
            .id(vo.getId())
            .name(vo.getName())
            .description(vo.getDescription())
            .domain(vo.getDomain())
            .icon(vo.getIcon())
            .build();
    }
}
