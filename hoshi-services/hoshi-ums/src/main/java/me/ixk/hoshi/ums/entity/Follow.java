/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * 关注表
 *
 * @author Otstar Lin
 * @date 2021/5/22 19:48
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Schema(name = "关注表")
@Entity
@Table(name = "follow")
public class Follow {

    /**
     * 主键
     */
    @Id
    @Schema(name = "ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Schema(name = "关注者 ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "id", nullable = false)
    @Schema(name = "关注对象 ID")
    private User following;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Follow follow = (Follow) o;
        return id != null && Objects.equals(id, follow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
