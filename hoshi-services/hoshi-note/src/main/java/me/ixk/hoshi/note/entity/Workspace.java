/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.mysql.generator.ObjectIdGenerator;
import me.ixk.hoshi.note.request.AddWorkspaceView;
import me.ixk.hoshi.note.request.UpdateWorkspaceView;
import me.ixk.hoshi.note.response.WorkspaceView;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 工作区
 *
 * @author Otstar Lin
 * @date 2021/11/17 20:45
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel("工作区表")
@Table(
    name = "workspace",
    indexes = {
        @Index(name = "idx_workspace_user_id", columnList = "user_id"),
        @Index(name = "idx_workspace_name", columnList = "name"),
        @Index(name = "idx_workspace_domain", columnList = "domain"),
    }
)
@Entity
public class Workspace {

    @Id
    @ApiModelProperty("空间 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(generator = "objectId")
    @GenericGenerator(name = "objectId", strategy = ObjectIdGenerator.STRATEGY_NAME)
    private String id;

    @ApiModelProperty("用户")
    @Column(name = "user_id", nullable = false)
    private Long user;

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

    @ApiModelProperty("是否公开")
    @Column(name = "disclose", nullable = false)
    private Boolean disclose;

    @ApiModelProperty("协作者")
    @OneToMany
    @JoinTable(
        name = "workspace_collaborator_relation",
        joinColumns = { @JoinColumn(name = "workspace_id", referencedColumnName = "id", nullable = false) },
        inverseJoinColumns = { @JoinColumn(name = "collaborator_id", referencedColumnName = "id", nullable = false) }
    )
    @JsonBackReference
    @ToString.Exclude
    private Set<Collaborator> collaborators;

    public static Workspace ofAdd(final AddWorkspaceView vo, final Long userId) {
        return Workspace
            .builder()
            .user(userId)
            .name(vo.getName())
            .description(vo.getDescription())
            .domain(vo.getDomain())
            .icon(vo.getIcon())
            .disclose(vo.getDisclose())
            .createdTime(OffsetDateTime.now())
            .build();
    }

    public static Workspace ofUpdate(final UpdateWorkspaceView vo) {
        return Workspace
            .builder()
            .id(vo.getId())
            .name(vo.getName())
            .description(vo.getDescription())
            .domain(vo.getDomain())
            .icon(vo.getIcon())
            .disclose(vo.getDisclose())
            .build();
    }

    public WorkspaceView toView() {
        return WorkspaceView
            .builder()
            .id(this.id)
            .user(this.user)
            .name(this.name)
            .description(this.description)
            .domain(this.domain)
            .icon(this.icon)
            .createdTime(this.createdTime)
            .disclose(this.disclose)
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
        Workspace workspace = (Workspace) o;
        return id != null && Objects.equals(id, workspace.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
