/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * 协作者
 *
 * @author Otstar Lin
 * @date 2021/11/17 22:29
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Schema(name = "协作表")
@Table(name = "collaborator", indexes = {@Index(name = "idx_collaborator_user", columnList = "user")})
@Entity
public class Collaborator {

    @Id
    @Schema(name = "协作 ID")
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "协作者 ID")
    @Column(name = "\"user\"")
    private Long user;

    @Schema(name = "权限 ID")
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Permission permission;

    public enum Permission {
        /**
         * 可读可写
         */
        READ_WRITE,
        /**
         * 可写
         */
        READ,
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Collaborator that = (Collaborator) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
