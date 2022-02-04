/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.request.AddRoleView;
import me.ixk.hoshi.ums.request.UpdateRoleView;
import me.ixk.hoshi.ums.response.RoleView;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * 角色表
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:37
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Schema(name = "角色表")
@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1210070802160893598L;

    /**
     * 角色名称，必须是大写英文
     */
    @Id
    @Schema(name = "角色名称，必须是大写英文")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @Column(name = "created_time", nullable = false)
    private OffsetDateTime createdTime;

    /**
     * 状态
     */
    @Schema(name = "状态")
    @Column(name = "status", nullable = false)
    private Boolean status;

    /**
     * 角色的描述
     */
    @Schema(name = "角色的描述")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 权限列表
     */
    @Schema(name = "权限列表")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> permissions;

    public static Role ofAdd(final AddRoleView vo) {
        return Role
            .builder()
            .name(vo.getName())
            .description(vo.getDescription())
            .permissions(vo.getPermissions())
            .status(vo.getStatus())
            .createdTime(OffsetDateTime.now())
            .build();
    }

    public static Role ofUpdate(final UpdateRoleView vo) {
        return Role
            .builder()
            .name(vo.getRoleName())
            .description(vo.getDescription())
            .permissions(vo.getPermissions())
            .status(vo.getStatus())
            .build();
    }

    public RoleView toView() {
        return RoleView
            .builder()
            .name(this.name)
            .createdTime(this.createdTime)
            .status(this.status)
            .description(this.description)
            .permissions(this.permissions)
            .build();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        final Role role = (Role) o;
        return name != null && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
