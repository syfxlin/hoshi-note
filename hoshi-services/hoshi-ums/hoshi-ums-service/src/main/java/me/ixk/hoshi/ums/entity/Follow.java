/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

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
@Entity
@ApiModel("关注表")
@Accessors(chain = true)
@Table(name = "user_follow_relation")
public class Follow {

    /**
     * 主键
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    @ApiModelProperty("关注者 ID")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "id")
    @ApiModelProperty("关注对象 ID")
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
